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
 * GEO AI mention entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_mention")
@Schema(description = "GEO AI mention")
public class GeoMention extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Monitor task id")
    private String monitorTaskId;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Engine")
    private String engine;

    @Schema(description = "Query")
    private String query;

    @Schema(description = "Occurred time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurredAt;

    @Schema(description = "Answer text")
    private String answerText;

    @Schema(description = "Mentioned: 0 no, 1 yes")
    private Integer mentioned;

    @Schema(description = "Position")
    private Integer position;

    @Schema(description = "Source urls json")
    private String sourceUrlsJson;

    @Schema(description = "Accuracy score")
    private Double accuracyScore;

    @Schema(description = "Sentiment")
    private String sentiment;

    @Schema(description = "Raw json")
    private String rawJson;
}
