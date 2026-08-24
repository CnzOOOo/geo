package org.jeecg.modules.geo.entity;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * GEO publish channel entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_channel")
@Schema(description = "GEO publish channel")
public class GeoChannel extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Platform")
    private String platform;

    @Schema(description = "Channel name")
    private String channelName;

    @Schema(description = "Encrypted config")
    private String configEncrypted;

    @Schema(description = "Enabled: 0 disabled, 1 enabled")
    private Integer enabled;

    @Schema(description = "Rate limit per day")
    private Integer rateLimit;

    @Schema(description = "Status: 0 init, 1 active, 2 failed")
    private Integer status;
}
