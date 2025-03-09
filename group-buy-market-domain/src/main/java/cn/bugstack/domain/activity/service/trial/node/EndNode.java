package cn.bugstack.domain.activity.service.trial.node;

import cn.bugstack.domain.activity.model.aggregate.TrialResponseAggerate;
import cn.bugstack.domain.activity.model.entity.*;
import cn.bugstack.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 正常结束节点
 * @create 2024-12-14 14:31
 */
@Slf4j
@Service
public class EndNode extends AbstractGroupBuyMarketSupport<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> {

    @Override
    public TrialResponseAggerate doApply(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-EndNode userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));


        // 获取上下文数据
        ActivityEntity activityEntity = dynamicContext.getActivityEntity();
        DiscountEntity discountEntity = dynamicContext.getDiscountEntity();
        GoodsEntity goodsEntity = dynamicContext.getGoodsEntity();
        UserEntity userEntity = dynamicContext.getUserEntity();

        // 返回空结果
        return TrialResponseAggerate.builder()
                .discountEntity(discountEntity)
                .goodsEntity(goodsEntity)
                .activityEntity(activityEntity)
                .userEntity(userEntity)
                .build();
    }

    @Override
    public StrategyHandler<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> get(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }

}
