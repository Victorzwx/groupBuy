package cn.bugstack.domain.activity.service.trial.node;

import cn.bugstack.domain.activity.model.aggregate.TrialResponseAggerate;
import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.TrialRequestEntity;
import cn.bugstack.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 人群标签判断
 * @create 2025-01-02 10:36
 */
@Slf4j
@Service
public class TagNode extends AbstractGroupBuyMarketSupport<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> {

    @Resource
    private EndNode endNode;

    @Override
    protected TrialResponseAggerate doApply(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 获取拼团活动配置
        ActivityEntity activityEntity = dynamicContext.getActivityEntity();

        String tagId = activityEntity.getTagId();
        boolean visible = activityEntity.isVisible();
        boolean enable = activityEntity.isEnable();

        // 人群标签配置为空，则走默认值
        if (StringUtils.isBlank(tagId)) {
            activityEntity.setIsEnable(true);
            activityEntity.setIsVisible(true);
            return router(requestParameter, dynamicContext);
        }

        // 是否在人群范围内；visible、enable 如果值为 ture 则表示没有配置拼团限制，那么就直接保证为 true 即可
        boolean isWithin = repository.isTagCrowdRange(tagId, requestParameter.getUserId());
        activityEntity.setIsVisible(visible || isWithin);
        activityEntity.setIsEnable(enable || isWithin);

        dynamicContext.setActivityEntity(activityEntity);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> get(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return endNode;
    }

}
