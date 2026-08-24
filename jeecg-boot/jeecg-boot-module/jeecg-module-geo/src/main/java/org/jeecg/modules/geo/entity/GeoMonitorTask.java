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
 * GEO monitor task entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_monitor_task")
@Schema(description = "GEO monitor task")
public class GeoMonitorTask extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Task name")
    private String name;

    @Schema(description = "Query set json")
    private String querySetJson;

    @Schema(description = "Engine config json")
    private String engineConfigJson;

    @Schema(description = "Cadence")
    private String cadence;

    @Schema(description = "Last run time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastRunAt;

    @Schema(description = "Enabled: 0 disabled, 1 enabled")
    private Integer enabled;

    @Schema(description = "Status")
    private Integer status;
}
