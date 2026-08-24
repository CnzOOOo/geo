package org.jeecg.modules.geo.entity;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * GEO question bank entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("geo_question_bank")
@Schema(description = "GEO question bank")
public class GeoQuestionBank extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Merchant id")
    private String merchantId;

    @Schema(description = "Question type: HOW, WHICH, COMPARE, AVOID, BRAND")
    private String questionType;

    @Schema(description = "Complete natural language question")
    private String question;

    @Schema(description = "User intent")
    private String intent;

    @Schema(description = "Region")
    private String region;

    @Schema(description = "Priority 1-5")
    private Integer priority;

    @Schema(description = "Question source")
    private String source;

    @Schema(description = "Status: 0 draft, 1 active, 2 disabled")
    private Integer status;
}
