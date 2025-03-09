package cn.bugstack.domain.activity.model.entity;

import cn.bugstack.domain.activity.model.valobj.TagScopeEnumVO;
import cn.bugstack.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEntity {


    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 活动名称
     */
    private String activityName;

    private Long discountId;

    /**
     * 拼团方式（0自动成团、1达成目标拼团）
     */
    private Integer groupType;
    /**
     * 拼团次数限制
     */
    private Integer takeLimitCount;
    /**
     * 拼团目标
     */
    private Integer target;
    /**
     * 拼团时长（分钟）
     */
    private Integer validTime;
    /**
     * 活动状态（0创建、1生效、2过期、3废弃）
     */
    private Integer status;
    /**
     * 活动开始时间
     */
    private Date startTime;
    /**
     * 活动结束时间
     */
    private Date endTime;
    /**
     * 人群标签规则标识
     */
    private String tagId;
    /**
     * 人群标签规则范围
     */
    private String tagScope;

    /** 是否可见拼团 */
    private Boolean isVisible;
    /** 是否可参与进团 */
    private Boolean isEnable;

    /**
     * 可见限制
     * 只要存在这样一个值，那么首次获得的默认值就是 false
     */
    public boolean isVisible() {
        if(StringUtils.isBlank(this.tagScope)) return TagScopeEnumVO.VISIBLE.getAllow();

        if (Objects.equals(this.tagScope.charAt(0),'1')) {
            return TagScopeEnumVO.VISIBLE.getRefuse();
        }
        return TagScopeEnumVO.VISIBLE.getAllow();
    }

    /**
     * 参与限制
     * 只要存在这样一个值，那么首次获得的默认值就是 false
     */
    public boolean isEnable() {
        if(StringUtils.isBlank(this.tagScope)) return TagScopeEnumVO.VISIBLE.getAllow();

        if (Objects.equals(this.tagScope.charAt(1),'1')) {
            return TagScopeEnumVO.ENABLE.getRefuse();
        }
        return TagScopeEnumVO.ENABLE.getAllow();
    }
}
