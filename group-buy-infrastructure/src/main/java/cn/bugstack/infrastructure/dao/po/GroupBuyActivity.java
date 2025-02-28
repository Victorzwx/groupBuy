package cn.bugstack.infrastructure.dao.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * 拼团活动实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBuyActivity {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 活动ID（唯一）
     */
    private Long activityId;

    /**
     * 活动名称（非空）
     */
    private String activityName;

    /**
     * 来源（非空，如：APP/WEB）
     */
    private String source;

    /**
     * 渠道（非空，如：IOS/Android/H5）
     */
    private String channel;

    /**
     * 商品ID（非空）
     */
    private String goodsId;

    /**
     * 折扣ID（非空）
     */
    private String discountId;

    /**
     * 拼团方式（默认0）
     * 0: 自动成团，1: 达成目标拼团
     */
    private Integer groupType;

    /**
     * 拼团次数限制（默认1）
     */
    private Integer takeLimitCount;

    /**
     * 拼团目标（默认1）
     */
    private Integer target;

    /**
     * 拼团时长（分钟，默认15）
     */
    private Integer validTime;

    /**
     * 活动状态（默认0）
     * 0: 创建，1: 生效，2: 过期，3: 废弃
     */
    private Integer status;

    /**
     * 活动开始时间（非空，默认当前时间）
     */
    private LocalDateTime startTime;

    /**
     * 活动结束时间（非空，默认当前时间）
     */
    private LocalDateTime endTime;

    /**
     * 人群标签规则标识
     */
    private String tagId;

    /**
     * 人群标签规则范围（多选）
     * 1: 可见限制，2: 参与限制
     */
    private String tagScope;

    /**
     * 创建时间（非空，默认当前时间）
     */
    private LocalDateTime createTime;

    /**
     * 更新时间（非空，默认当前时间，自动更新）
     */
    private LocalDateTime updateTime;
}
