package cn.bugstack.domain.activity.service.trial.thread;

import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.aggregate.ActivityGoodsAggerate;
import cn.bugstack.domain.activity.model.valobj.SCSkuActivityVO;

import java.util.concurrent.Callable;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 查询营销配置任务
 * @create 2024-12-21 09:46
 */
public class QueryActivityGoodsAggerateThreadTask implements Callable<ActivityGoodsAggerate> {

    /**
     * 活动ID
     */
    private final Long activityId;

    /**
     * 来源
     */
    private final String source;

    /**
     * 渠道
     */
    private final String channel;

    /**
     * 商品ID
     */
    private final String goodsId;

    /**
     * 活动仓储
     */
    private final IActivityRepository activityRepository;

    public QueryActivityGoodsAggerateThreadTask(Long activityId, String source, String channel, String goodsId, IActivityRepository activityRepository) {
        this.activityId = activityId;
        this.source = source;
        this.channel = channel;
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityGoodsAggerate call() throws Exception {
        // 判断是否存在可用的活动ID
        Long availableActivityId = activityId;
        if (null == activityId){
            // 查询渠道商品活动配置关联配置
            SCSkuActivityVO scSkuActivityVO = activityRepository.querySCSkuActivityBySCGoodsId(source, channel, goodsId);
            if (null == scSkuActivityVO) return null;
            availableActivityId = scSkuActivityVO.getActivityId();
        }
        // 查询活动配置
        return activityRepository.queryGroupBuyActivityDiscountAggerate(availableActivityId);
    }

}
