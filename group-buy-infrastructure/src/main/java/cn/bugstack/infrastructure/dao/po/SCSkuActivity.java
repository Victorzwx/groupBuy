package cn.bugstack.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SCSkuActivity {
    /** 自增ID */
    private Long id;
    /** 来源 */
    private String source;
    /** 渠道 */
    private String channel;
    /** 商品ID */
    private String goodsId;
    /** 活动ID */
    private Long activityId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
