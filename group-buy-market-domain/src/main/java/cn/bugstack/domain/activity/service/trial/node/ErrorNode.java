package cn.bugstack.domain.activity.service.trial.node;

import cn.bugstack.domain.activity.model.aggregate.TrialResponseAggerate;
import cn.bugstack.domain.activity.model.entity.TrialRequestEntity;
import cn.bugstack.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 异常节点处理；无营销、流程降级、超时调用等，都可以路由到 ErrorNode 节点统一处理
 * @create 2025-01-01 13:47
 */
@Slf4j
@Service
public class ErrorNode extends AbstractGroupBuyMarketSupport<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> {

    @Override
    protected TrialResponseAggerate doApply(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-NoMarketNode userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));

        // 无营销配置
        if (null == dynamicContext.getGoodsEntity() || null == dynamicContext.getDiscountEntity() || null == dynamicContext.getUserEntity() || null == dynamicContext.getActivityEntity()) {
            log.info("商品无拼团营销配置 {}", requestParameter.getGoodsId());
            throw new AppException(ResponseCode.E0002.getCode(), ResponseCode.E0002.getInfo());
        }

        return TrialResponseAggerate.builder().build();
    }

    @Override
    public StrategyHandler<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> get(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }

}
