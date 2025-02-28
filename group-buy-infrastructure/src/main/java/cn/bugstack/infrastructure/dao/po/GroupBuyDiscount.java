package cn.bugstack.infrastructure.dao.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * 拼团折扣实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBuyDiscount {
    /**
     * 自增ID（主键）
     */
    private Long id;

    /**
     * 折扣ID（唯一）
     */
    private Integer discountId;

    /**
     * 折扣标题（非空）
     */
    private String discountName;

    /**
     * 折扣描述（非空）
     */
    private String discountDesc;

    /**
     * 折扣类型（默认0）
     * 0: base，1: tag
     */
    private Integer discountType;

    /**
     * 营销优惠计划（默认ZJ）
     * ZJ: 直减，MJ: 满减，N元购
     */
    private String marketPlan;

    /**
     * 营销优惠表达式（非空）
     */
    private String marketExpr;

    /**
     * 人群标签，特定优惠限定
     */
    private String tagId;

    /**
     * 创建时间（非空，默认当前时间）
     */
    private LocalDateTime createTime;

    /**
     * 更新时间（非空，默认当前时间，自动更新）
     */
    private LocalDateTime updateTime;
}