package cn.bugstack.domain.activity.service.trial;

import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.entity.TrialRequestEntity;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.types.design.framework.tree.AbstractMultiThreadStrategyRouter;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽象的拼团营销支撑类
 * @create 2024-12-14 13:42
 */
public abstract class AbstractGroupBuyMarketSupport<MarketProductEntity, DynamicContext, TrialResponseAggerate> extends AbstractMultiThreadStrategyRouter<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> {

    protected long timeout = 500;
    @Resource
    protected IActivityRepository repository;

    @Override
    protected void multiThread(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 缺省的方法
    }

}
