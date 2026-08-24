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
 * GEO experiment entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_experiment")
@Schema(description = "GEO experiment")
public class GeoExperiment extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Experiment name")
    private String name;

    @Schema(description = "Control group json")
    private String controlGroupJson;

    @Schema(description = "Variant group json")
    private String variantGroupJson;

    @Schema(description = "Status")
    private Integer status;

    @Schema(description = "Started time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startedAt;

    @Schema(description = "Ended time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endedAt;

    @Schema(description = "Conclusion")
    private String conclusion;
}
