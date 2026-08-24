package org.jeecg.modules.geo.vo;

import lombok.Data;

/**
 * GEO article generation request.
 */
@Data
public class GeoArticleGenerateRequest {

    private String merchantId;

    private String questionId;

    private String title;

    private String titleType;
}
