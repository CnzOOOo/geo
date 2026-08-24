package org.jeecg.modules.geo.publish;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoChannel;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Generic webhook publish adapter.
 */
@Component
public class WebhookGeoPublishAdapter implements GeoPublishAdapter {

    @Override
    public boolean supports(String platform, GeoChannel channel) {
        return "webhook".equalsIgnoreCase(platform);
    }

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public GeoPublishResult publish(GeoArticle article, GeoChannel channel) throws Exception {
        if (channel.getConfigEncrypted() == null || channel.getConfigEncrypted().isBlank()) {
            return GeoPublishResult.fail("CONFIG_MISSING", "webhook channel config is empty");
        }
        JSONObject config = JSON.parseObject(channel.getConfigEncrypted());
        String url = config.getString("url");
        if (url == null || url.isBlank()) {
            return GeoPublishResult.fail("URL_MISSING", "webhook url is missing");
        }

        JSONObject payload = new JSONObject();
        payload.put("title", article.getTitle());
        payload.put("summary", article.getSummary());
        payload.put("content", article.getContentMd());
        payload.put("canonicalUrl", article.getCanonicalUrl());

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json");

        JSONObject headers = config.getJSONObject("headers");
        if (headers != null) {
            headers.forEach((key, value) -> builder.header(key, String.valueOf(value)));
        }

        String method = config.getString("method");
        if (method == null || method.isBlank()) {
            method = "POST";
        }
        if ("PUT".equalsIgnoreCase(method)) {
            builder.PUT(HttpRequest.BodyPublishers.ofString(payload.toJSONString(), StandardCharsets.UTF_8));
        } else {
            builder.POST(HttpRequest.BodyPublishers.ofString(payload.toJSONString(), StandardCharsets.UTF_8));
        }

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return GeoPublishResult.ok(null, response.body());
        }
        return GeoPublishResult.fail("HTTP_" + response.statusCode(), response.body());
    }
}
