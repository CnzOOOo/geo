package org.jeecg.modules.geo.publish;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoChannel;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Bridge adapter for the externally installed Wechatsync CLI.
 */
@Component
public class WechatsyncCliPublishAdapter implements GeoPublishAdapter {

    private static final Set<String> PLATFORMS = Set.of(
            "zhihu",
            "juejin",
            "jianshu",
            "toutiao",
            "weibo",
            "bilibili",
            "baijiahao",
            "csdn",
            "yuque",
            "douban",
            "sohu",
            "xueqiu",
            "woshipm",
            "dayu",
            "yidian",
            "51cto",
            "sohufocus",
            "imooc",
            "oschina",
            "segmentfault",
            "cnblogs",
            "eastmoney",
            "smzdm",
            "netease",
            "wangyi",
            "weixin",
            "x",
            "xiaohongshu",
            "douyin"
    );

    @Override
    public boolean supports(String platform, GeoChannel channel) {
        if (platform == null || !PLATFORMS.contains(platform.toLowerCase())) {
            return false;
        }
        try {
            JSONObject config = parseConfig(channel);
            return Boolean.TRUE.equals(config.getBoolean("wechatsyncEnabled"))
                    || "true".equalsIgnoreCase(config.getString("wechatsyncEnabled"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public GeoPublishResult publish(GeoArticle article, GeoChannel channel) throws Exception {
        if (channel.getPlatform() == null || channel.getPlatform().isBlank()) {
            return GeoPublishResult.fail("PLATFORM_MISSING", "platform is empty");
        }
        JSONObject config = parseConfig(channel);
        String cliPath = config.getString("cliPath");
        if (cliPath == null || cliPath.isBlank()) {
            cliPath = WechatsyncCliPathResolver.resolve();
        }
        String token = config.getString("token");
        if (token == null || token.isBlank()) {
            token = System.getenv("WECHATSYNC_TOKEN");
        }
        if (token == null || token.isBlank()) {
            return GeoPublishResult.fail("WECHATSYNC_TOKEN_MISSING", "渠道配置或环境变量需要 WECHATSYNC_TOKEN");
        }

        Path markdownFile = Files.createTempFile("geo-wechatsync-", ".md");
        try {
            String content = buildMarkdown(article);
            Files.writeString(markdownFile, content, StandardCharsets.UTF_8);

            List<String> command = new ArrayList<>();
            command.add(cliPath);
            command.add("sync");
            command.add(markdownFile.toString());
            command.add("-p");
            command.add(channel.getPlatform());
            if (article.getTitle() != null && !article.getTitle().isBlank()) {
                command.add("-t");
                command.add(article.getTitle());
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Map<String, String> env = processBuilder.environment();
            env.put("WECHATSYNC_TOKEN", token);
            if (config.getString("wsPort") != null && !config.getString("wsPort").isBlank()) {
                env.put("SYNC_WS_PORT", config.getString("wsPort"));
            }
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            boolean finished = process.waitFor(120, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return GeoPublishResult.fail("WECHATSYNC_TIMEOUT", "wechatsync cli timeout");
            }
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (process.exitValue() == 0) {
                String externalUrl = extractDraftUrl(output);
                return GeoPublishResult.ok("wechatsync:" + channel.getPlatform(),
                        externalUrl == null ? output.trim() : externalUrl);
            }
            return GeoPublishResult.fail("WECHATSYNC_CLI_FAILED", output);
        } finally {
            Files.deleteIfExists(markdownFile);
        }
    }

    private String extractDraftUrl(String output) {
        Matcher matcher = Pattern.compile("https?://\\S+").matcher(output);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private JSONObject parseConfig(GeoChannel channel) {
        if (channel.getConfigEncrypted() == null || channel.getConfigEncrypted().isBlank()) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(channel.getConfigEncrypted());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private String buildMarkdown(GeoArticle article) {
        StringBuilder content = new StringBuilder();
        if (article.getTitle() != null && !article.getTitle().isBlank()) {
            content.append("# ").append(article.getTitle()).append("\n\n");
        }
        if (article.getContentMd() != null) {
            content.append(article.getContentMd());
        }
        return content.toString();
    }
}
