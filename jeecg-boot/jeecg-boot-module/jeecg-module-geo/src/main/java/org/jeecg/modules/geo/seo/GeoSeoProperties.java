package org.jeecg.modules.geo.seo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * GEO site properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "geo.site")
public class GeoSeoProperties {

    private String baseUrl = "";

    private String siteName = "";
}
