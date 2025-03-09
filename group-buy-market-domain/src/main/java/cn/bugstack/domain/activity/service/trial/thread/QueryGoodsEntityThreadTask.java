package cn.bugstack.domain.activity.service.trial.thread;

import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.entity.GoodsEntity;

import java.util.concurrent.Callable;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 查询商品信息任务
 * @create 2024-12-21 10:51
 */
public class QueryGoodsEntityThreadTask implements Callable<GoodsEntity> {

    private final String goodsId;

    private final IActivityRepository activityRepository;

    public QueryGoodsEntityThreadTask(String goodsId, IActivityRepository activityRepository) {
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }

    @Override
    public GoodsEntity call() throws Exception {
        return activityRepository.querySkuByGoodsId(goodsId);
    }

}
