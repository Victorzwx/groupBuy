package cn.bugstack.domain.activity.service.trial.factory;

import cn.bugstack.domain.activity.model.aggregate.TrialResponseAggerate;
import cn.bugstack.domain.activity.model.entity.*;

import cn.bugstack.domain.activity.service.trial.node.RootNode;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动策略工厂
 * @create 2024-12-14 13:41
 */
@Service
public class DefaultActivityStrategyFactory {

    private final RootNode rootNode;

    public DefaultActivityStrategyFactory(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    public StrategyHandler<TrialRequestEntity, DynamicContext, TrialResponseAggerate> strategyHandler() {
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        private ActivityEntity activityEntity;
        private GoodsEntity goodsEntity;
        private DiscountEntity discountEntity;
        private UserEntity userEntity;
    }

}
