package org.jeecg.modules.geo.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * GEO AI provider properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "geo.ai")
public class GeoAiProperties {

    private String provider = "openai-compatible";

    private String baseUrl = "https://api.deepseek.com/v1";

    private String apiKey = "";

    private String model = "deepseek-chat";

    private int timeoutSeconds = 60;
}
