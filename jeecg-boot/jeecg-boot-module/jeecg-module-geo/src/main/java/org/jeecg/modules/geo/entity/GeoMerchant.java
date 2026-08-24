package org.jeecg.modules.geo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jeecg.common.system.base.entity.JeecgEntity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * GEO merchant entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_merchant")
@Schema(description = "GEO merchant")
public class GeoMerchant extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Tenant id")
    private Integer tenantId;

    @Schema(description = "Merchant name")
    private String merchantName;

    @Schema(description = "Aliases")
    private String aliases;

    @Schema(description = "Category")
    private String category;

    @Schema(description = "Province")
    private String province;

    @Schema(description = "City")
    private String city;

    @Schema(description = "District")
    private String district;

    @Schema(description = "Address")
    private String address;

    @Schema(description = "Longitude")
    private BigDecimal lng;

    @Schema(description = "Latitude")
    private BigDecimal lat;

    @Schema(description = "Phone")
    private String phone;

    @Schema(description = "Opening hours")
    private String openingHours;

    @Schema(description = "Service area")
    private String serviceArea;

    @Schema(description = "Website")
    private String website;

    @Schema(description = "Mini program")
    private String miniProgram;

    @Schema(description = "Logo")
    private String logo;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Status: 0 draft, 1 active, 2 inactive")
    private Integer status;
}
