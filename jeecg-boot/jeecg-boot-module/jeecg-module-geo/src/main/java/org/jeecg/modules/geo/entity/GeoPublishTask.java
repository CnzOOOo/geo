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
 * GEO publish task entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_publish_task")
@Schema(description = "GEO publish task")
public class GeoPublishTask extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Article id")
    private String articleId;

    @Schema(description = "Channel id")
    private String channelId;

    @Schema(description = "Status: 0 queued, 1 publishing, 2 success, 3 failed, 4 manual")
    private Integer status;

    @Schema(description = "External id")
    private String externalId;

    @Schema(description = "External url")
    private String externalUrl;

    @Schema(description = "Error code")
    private String errorCode;

    @Schema(description = "Error message")
    private String errorMsg;

    @Schema(description = "Retry count")
    private Integer retryCount;

    @Schema(description = "Scheduled time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduledAt;

    @Schema(description = "Published time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishedAt;
}
