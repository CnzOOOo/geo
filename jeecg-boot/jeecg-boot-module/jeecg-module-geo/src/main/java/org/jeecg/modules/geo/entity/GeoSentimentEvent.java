package org.jeecg.modules.geo.entity;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * GEO sentiment event entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_sentiment_event")
@Schema(description = "GEO sentiment event")
public class GeoSentimentEvent extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Platform")
    private String platform;

    @Schema(description = "Event type")
    private String eventType;

    @Schema(description = "Title")
    private String title;

    @Schema(description = "Content")
    private String content;

    @Schema(description = "Sentiment")
    private String sentiment;

    @Schema(description = "Severity")
    private Integer severity;

    @Schema(description = "Status")
    private Integer status;

    @Schema(description = "Owner id")
    private String ownerId;

    @Schema(description = "Source url")
    private String sourceUrl;
}
