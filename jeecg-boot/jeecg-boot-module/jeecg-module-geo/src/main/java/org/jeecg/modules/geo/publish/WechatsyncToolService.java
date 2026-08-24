package org.jeecg.modules.geo.publish;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jeecg.modules.geo.entity.GeoChannel;
import org.jeecg.modules.geo.service.IGeoChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Wechatsync environment check and maintenance service.
 */
@Service
public class WechatsyncToolService {

    private static final String NPM_MIRROR = "https://registry.npmmirror.com";
    private static final String CHROME_WEB_STORE_URL = "https://chromewebstore.google.com/detail/微信同步助手/hchobocdmclopcbnibdnoafilagadion";
    private static final String INSTALL_PAGE_URL = "https://wechatsync.com/#install";
    private static final String FALLBACK_PLUGIN_URL = "https://wpics.oss-cn-shanghai.aliyuncs.com/wechatsync-2.0.9.zip?date=20260324";

    @Autowired
    private IGeoChannelService geoChannelService;

    public JSONObject status() {
        String cliPath = findCliPath();
        JSONObject result = new JSONObject();
        result.put("cliPath", cliPath);

        JSONObject versionRun = runCommand(List.of(cliPath, "--version"), Map.of(), 10);
        boolean cliInstalled = versionRun.getBooleanValue("success") && versionRun.getIntValue("exitCode") == 0;
        result.put("cliInstalled", cliInstalled);
        result.put("cliVersion", cliInstalled ? firstLine(versionRun.getString("output")) : null);

        String token = findToken();
        String wsPort = findWsPort();
        result.put("tokenConfigured", token != null && !token.isBlank());
        result.put("wsPort", wsPort);

        JSONObject notRun = new JSONObject();
        notRun.put("success", false);
        notRun.put("output", "未检查，请点击“检查平台登录”");
        result.put("platformStatus", notRun);

        JSONArray issues = new JSONArray();
        if (!cliInstalled) {
            issues.add("Wechatsync CLI 未安装");
        }
        if (token == null || token.isBlank()) {
            issues.add("尚未在发布渠道配置 wechatsync token");
        }
        if (wsPort == null || wsPort.isBlank()) {
            issues.add("尚未配置 wsPort，将使用默认 9527");
        }
        result.put("issues", issues);
        return result;
    }

    public JSONObject platformStatus() {
        String cliPath = findCliPath();
        JSONObject versionRun = runCommand(List.of(cliPath, "--version"), Map.of(), 10);
        boolean cliInstalled = versionRun.getBooleanValue("success") && versionRun.getIntValue("exitCode") == 0;
        if (!cliInstalled) {
            JSONObject result = new JSONObject();
            result.put("success", false);
            result.put("output", "CLI 未安装，无法检查平台登录状态");
            return result;
        }

        String token = findToken();
        Map<String, String> env = Map.of();
        if (token != null && !token.isBlank()) {
            env = Map.of("WECHATSYNC_TOKEN", token);
        }
        return runCommand(List.of(cliPath, "platforms", "--auth"), env, 30);
    }

    public JSONObject install() {
        String npm = npmCommand();
        JSONObject run = runCommand(
                List.of(npm, "install", "-g", "@wechatsync/cli", "--registry=" + NPM_MIRROR),
                Map.of(),
                300
        );
        return run;
    }

    public JSONObject update() {
        String npm = npmCommand();
        JSONObject run = runCommand(
                List.of(npm, "install", "-g", "@wechatsync/cli@latest", "--registry=" + NPM_MIRROR),
                Map.of(),
                300
        );
        return run;
    }

    public JSONObject checkUpdate() {
        JSONObject result = new JSONObject();
        JSONObject status = status();
        String current = status.getString("cliVersion");
        result.put("currentVersion", current);

        String latest = fetchLatestVersion();
        result.put("latestVersion", latest);
        result.put("updateAvailable", latest != null && !latest.equalsIgnoreCase(current));
        return result;
    }

    public JSONObject pluginInfo() {
        JSONObject info = new JSONObject();
        info.put("chromeWebStoreUrl", CHROME_WEB_STORE_URL);
        info.put("installPageUrl", INSTALL_PAGE_URL);

        String downloadUrl = fetchLatestReleaseDownloadUrl();
        info.put("downloadUrl", downloadUrl == null ? FALLBACK_PLUGIN_URL : downloadUrl);

        JSONArray steps = new JSONArray();
        steps.add("优先点击“Chrome 官方安装入口”，在 Chrome Web Store 中安装插件。");
        steps.add("如果无法访问商店，点击“下载插件包”并解压。");
        steps.add("打开 chrome://extensions，开启右上角“开发者模式”。");
        steps.add("点击“加载已解压的扩展程序”，选择解压后的插件目录。");
        steps.add("在扩展设置中开启“同步桥接”，配置 Token 和端口。");
        info.put("steps", steps);
        return info;
    }

    private String findCliPath() {
        return WechatsyncCliPathResolver.resolve();
    }

    private String findToken() {
        String envToken = System.getenv("WECHATSYNC_TOKEN");
        if (envToken != null && !envToken.isBlank()) {
            return envToken;
        }
        for (GeoChannel channel : geoChannelService.list()) {
            JSONObject config = parseConfig(channel);
            if (isEnabled(config) && config.getString("token") != null && !config.getString("token").isBlank()) {
                return config.getString("token");
            }
        }
        return null;
    }

    private String findWsPort() {
        String envPort = System.getenv("SYNC_WS_PORT");
        if (envPort != null && !envPort.isBlank()) {
            return envPort;
        }
        for (GeoChannel channel : geoChannelService.list()) {
            JSONObject config = parseConfig(channel);
            if (isEnabled(config) && config.getString("wsPort") != null && !config.getString("wsPort").isBlank()) {
                return config.getString("wsPort");
            }
        }
        return null;
    }

    private boolean isEnabled(JSONObject config) {
        return Boolean.TRUE.equals(config.getBoolean("wechatsyncEnabled"))
                || "true".equalsIgnoreCase(config.getString("wechatsyncEnabled"));
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

    private JSONObject runCommand(List<String> command, Map<String, String> extraEnv, long timeoutSeconds) {
        JSONObject result = new JSONObject();
        result.put("command", String.join(" ", command));
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Map<String, String> env = builder.environment();
            if (extraEnv != null) {
                env.putAll(extraEnv);
            }
            builder.redirectErrorStream(true);
            Process process = builder.start();
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                result.put("success", false);
                result.put("exitCode", -1);
                result.put("output", "命令超时");
                return result;
            }
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            result.put("success", process.exitValue() == 0);
            result.put("exitCode", process.exitValue());
            result.put("output", output);
        } catch (IOException e) {
            result.put("success", false);
            result.put("exitCode", -1);
            result.put("output", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.put("success", false);
            result.put("exitCode", -1);
            result.put("output", e.getMessage());
        }
        return result;
    }

    private String npmCommand() {
        String os = System.getProperty("os.name", "").toLowerCase();
        return os.contains("win") ? "npm.cmd" : "npm";
    }

    private String fetchLatestVersion() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(NPM_MIRROR + "/@wechatsync/cli/latest"))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                JSONObject json = JSON.parseObject(response.body());
                return json.getString("version");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private String fetchLatestReleaseDownloadUrl() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repos/wechatsync/Wechatsync/releases/latest"))
                    .header("Accept", "application/vnd.github+json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                JSONObject json = JSON.parseObject(response.body());
                JSONArray assets = json.getJSONArray("assets");
                if (assets != null) {
                    for (int i = 0; i < assets.size(); i++) {
                        JSONObject asset = assets.getJSONObject(i);
                        String name = asset.getString("name");
                        if (name != null && name.toLowerCase().endsWith(".zip")) {
                            return asset.getString("browser_download_url");
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private String firstLine(String value) {
        if (value == null) {
            return null;
        }
        String[] lines = value.split("\n");
        for (String line : lines) {
            if (line != null && !line.isBlank()) {
                return line.trim();
            }
        }
        return value;
    }
}
