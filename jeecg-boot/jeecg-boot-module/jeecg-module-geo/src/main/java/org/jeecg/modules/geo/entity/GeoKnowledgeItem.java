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
 * GEO knowledge item entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_knowledge_item")
@Schema(description = "GEO knowledge item")
public class GeoKnowledgeItem extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Category")
    private String category;

    @Schema(description = "Fact name")
    private String fact;

    @Schema(description = "Fact value")
    private String value;

    @Schema(description = "Source type")
    private String sourceType;

    @Schema(description = "Source url")
    private String sourceUrl;

    @Schema(description = "Owner id")
    private String ownerId;

    @Schema(description = "Verified time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date verifiedAt;

    @Schema(description = "Valid from")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validFrom;

    @Schema(description = "Valid to")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validTo;

    @Schema(description = "Status: 0 draft, 1 verified, 2 expired, 3 disabled")
    private Integer status;
}
