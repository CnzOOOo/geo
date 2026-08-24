package org.jeecg.modules.geo.publish;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * WeChat Official Account draft and publish adapter.
 */
@Component
public class WechatOfficialAccountPublishAdapter implements GeoPublishAdapter {

    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String DRAFT_ADD_URL = "https://api.weixin.qq.com/cgi-bin/draft/add";
    private static final String FREE_PUBLISH_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/submit";

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean supports(String platform, GeoChannel channel) {
        return "wechat_mp".equalsIgnoreCase(platform) || "wechat".equalsIgnoreCase(platform);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public GeoPublishResult publish(GeoArticle article, GeoChannel channel) throws Exception {
        JSONObject config = parseConfig(channel);
        String appId = config.getString("appId");
        String appSecret = config.getString("appSecret");
        if (isBlank(appId) || isBlank(appSecret)) {
            return GeoPublishResult.fail("CONFIG_MISSING", "wechat_mp appId/appSecret are required");
        }

        String accessToken = getAccessToken(appId, appSecret);
        String thumbMediaId = config.getString("thumbMediaId");
        if (isBlank(thumbMediaId)) {
            return GeoPublishResult.fail("NEED_THUMB_MEDIA_ID", "请先在公众号素材库上传封面，并在渠道配置 thumbMediaId");
        }

        String mediaId = addDraft(accessToken, article, config, thumbMediaId);
        String publishId = submitFreePublish(accessToken, mediaId);
        return GeoPublishResult.ok(publishId, null);
    }

    private JSONObject parseConfig(GeoChannel channel) {
        if (channel.getConfigEncrypted() == null || channel.getConfigEncrypted().isBlank()) {
            throw new IllegalStateException("wechat_mp channel config is empty");
        }
        try {
            return JSON.parseObject(channel.getConfigEncrypted());
        } catch (Exception e) {
            throw new IllegalStateException("wechat_mp channel config is invalid json");
        }
    }

    private String getAccessToken(String appId, String appSecret) throws Exception {
        String cacheKey = "geo:wechat:access_token:" + appId;
        Object cached = redisUtil.get(cacheKey);
        if (cached != null) {
            return String.valueOf(cached);
        }

        String url = TOKEN_URL
                + "?grant_type=client_credential"
                + "&appid=" + URLEncoder.encode(appId, StandardCharsets.UTF_8)
                + "&secret=" + URLEncoder.encode(appSecret, StandardCharsets.UTF_8);
        JSONObject result = JSON.parseObject(get(url));
        checkError(result);
        String token = result.getString("access_token");
        if (isBlank(token)) {
            throw new IllegalStateException("wechat access_token is empty");
        }
        redisUtil.set(cacheKey, token, 7000);
        return token;
    }

    private String addDraft(String accessToken, GeoArticle article, JSONObject config, String thumbMediaId) throws Exception {
        JSONObject articleBody = new JSONObject();
        articleBody.put("title", article.getTitle());
        articleBody.put("content", markdownToHtml(article.getContentMd()));
        articleBody.put("thumb_media_id", thumbMediaId);
        if (!isBlank(config.getString("author"))) {
            articleBody.put("author", config.getString("author"));
        }
        if (!isBlank(config.getString("digest"))) {
            articleBody.put("digest", config.getString("digest"));
        } else if (!isBlank(article.getSummary())) {
            articleBody.put("digest", article.getSummary());
        }
        if (!isBlank(article.getCanonicalUrl())) {
            articleBody.put("content_source_url", article.getCanonicalUrl());
        }
        int needOpenComment = config.containsKey("needOpenComment") ? config.getIntValue("needOpenComment") : 1;
        int onlyFansCanComment = config.containsKey("onlyFansCanComment") ? config.getIntValue("onlyFansCanComment") : 0;
        articleBody.put("need_open_comment", needOpenComment);
        articleBody.put("only_fans_can_comment", onlyFansCanComment);

        JSONArray articles = new JSONArray();
        articles.add(articleBody);
        JSONObject body = new JSONObject();
        body.put("articles", articles);

        JSONObject result = JSON.parseObject(post(DRAFT_ADD_URL + "?access_token=" + accessToken, body));
        checkError(result);
        String mediaId = result.getString("media_id");
        if (isBlank(mediaId)) {
            throw new IllegalStateException("wechat draft media_id is empty");
        }
        return mediaId;
    }

    private String submitFreePublish(String accessToken, String mediaId) throws Exception {
        JSONObject body = new JSONObject();
        body.put("media_id", mediaId);
        JSONObject result = JSON.parseObject(post(FREE_PUBLISH_URL + "?access_token=" + accessToken, body));
        checkError(result);
        String publishId = result.getString("publish_id");
        if (isBlank(publishId)) {
            throw new IllegalStateException("wechat publish_id is empty");
        }
        return publishId;
    }

    private void checkError(JSONObject result) {
        int errcode = result.getIntValue("errcode");
        if (errcode != 0) {
            throw new IllegalStateException("wechat api error " + errcode + ": " + result.getString("errmsg"));
        }
    }

    private String get(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();
        return send(request);
    }

    private String post(String url, JSONObject body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString(), StandardCharsets.UTF_8))
                .build();
        return send(request);
    }

    private String send(HttpRequest request) throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("wechat http error " + response.statusCode() + ": " + response.body());
        }
        return response.body();
    }

    private String markdownToHtml(String markdown) {
        if (isBlank(markdown)) {
            return "<p></p>";
        }
        StringBuilder html = new StringBuilder();
        for (String line : markdown.split("\n")) {
            String text = line.trim();
            if (text.isEmpty()) {
                continue;
            }
            if (text.startsWith("### ")) {
                html.append("<h3>").append(escape(text.substring(4))).append("</h3>");
            } else if (text.startsWith("## ")) {
                html.append("<h2>").append(escape(text.substring(3))).append("</h2>");
            } else if (text.startsWith("# ")) {
                html.append("<h1>").append(escape(text.substring(2))).append("</h1>");
            } else if (text.startsWith("- ")) {
                html.append("<p>• ").append(escape(text.substring(2))).append("</p>");
            } else {
                html.append("<p>").append(escape(text)).append("</p>");
            }
        }
        return html.toString();
    }

    private String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
