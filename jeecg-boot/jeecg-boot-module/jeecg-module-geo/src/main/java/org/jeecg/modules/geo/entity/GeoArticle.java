package org.jeecg.modules.geo.entity;

import java.io.Serializable;
import java.util.Date;

import org.jeecg.common.system.base.entity.JeecgEntity;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * GEO article entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_article")
@Schema(description = "GEO article")
public class GeoArticle extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Question bank id")
    private String questionId;

    @Schema(description = "Title")
    private String title;

    @Schema(description = "Title type: QUESTION, COMPARE, AVOID")
    private String titleType;

    @Schema(description = "Summary")
    private String summary;

    @Schema(description = "Markdown content")
    private String contentMd;

    @Schema(description = "Status: 0 draft, 1 pending review, 2 published, 3 offline")
    private Integer status;

    @Schema(description = "Review status: 0 not reviewed, 1 reviewing, 2 passed, 3 rejected")
    private Integer reviewStatus;

    @Schema(description = "Reviewer id")
    private String reviewerId;

    @Schema(description = "Published time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishedAt;

    @Schema(description = "Canonical url")
    private String canonicalUrl;

    @Schema(description = "E-E-A-T experience score 1-10")
    private Integer eeatExperienceScore;

    @Schema(description = "E-E-A-T expertise score 1-10")
    private Integer eeatExpertiseScore;

    @Schema(description = "E-E-A-T authority score 1-10")
    private Integer eeatAuthorityScore;

    @Schema(description = "E-E-A-T trust score 1-10")
    private Integer eeatTrustScore;
}
