package cn.bugstack.domain.activity.service.trial.node;

import cn.bugstack.domain.activity.model.aggregate.ActivityGoodsAggerate;
import cn.bugstack.domain.activity.model.aggregate.TrialResponseAggerate;
import cn.bugstack.domain.activity.model.entity.*;
import cn.bugstack.domain.activity.service.discount.IDiscountCalculateService;
import cn.bugstack.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.domain.activity.service.trial.thread.QueryActivityGoodsAggerateThreadTask;
import cn.bugstack.domain.activity.service.trial.thread.QueryGoodsEntityThreadTask;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 营销优惠节点
 * @create 2024-12-14 14:30
 */
@Slf4j
@Service
public class MarketNode extends AbstractGroupBuyMarketSupport<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    /**
     * <a href="https://bugstack.cn/md/road-map/spring-dependency-injection.html">Spring 注入详细说明</a>
     */
    @Resource
    private Map<String, IDiscountCalculateService> discountCalculateServiceMap;
    @Resource
    private ErrorNode errorNode;
    @Resource
    private TagNode tagNode;

    @Override
    protected void multiThread(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {

        // 异步查询活动配置
        QueryActivityGoodsAggerateThreadTask queryActivityGoodsAggerateThreadTask = new QueryActivityGoodsAggerateThreadTask(requestParameter.getActivityId(), requestParameter.getSource(), requestParameter.getChannel(), requestParameter.getGoodsId(), repository);
        FutureTask<ActivityGoodsAggerate> activityDiscountAggerateFutureTask = new FutureTask<>(queryActivityGoodsAggerateThreadTask);
        threadPoolExecutor.execute(activityDiscountAggerateFutureTask);

        // 异步查询商品信息 - 在实际生产中，商品有同步库或者调用接口查询。这里暂时使用DB方式查询。
        QueryGoodsEntityThreadTask queryGoodsEntityThreadTask = new QueryGoodsEntityThreadTask(requestParameter.getGoodsId(), repository);
        FutureTask<GoodsEntity> goodsEntityFutureTask = new FutureTask<>(queryGoodsEntityThreadTask);
        threadPoolExecutor.execute(goodsEntityFutureTask);

        // 写入上下文 - 对于一些复杂场景，获取数据的操作，有时候会在下N个节点获取，这样前置查询数据，可以提高接口响应效率
        ActivityGoodsAggerate activityGoodsAggerate = activityDiscountAggerateFutureTask.get(timeout, TimeUnit.MINUTES);
        GoodsEntity goodsEntity = goodsEntityFutureTask.get(timeout, TimeUnit.MINUTES);
        if(null == activityGoodsAggerate || null == goodsEntity){
            router(requestParameter, dynamicContext);
            return;
        }

        dynamicContext.setActivityEntity(activityGoodsAggerate.getActivityEntity());
        dynamicContext.setDiscountEntity(activityGoodsAggerate.getDiscountEntity());
        dynamicContext.setGoodsEntity(goodsEntity);
        dynamicContext.setUserEntity(UserEntity.builder().userId(requestParameter.getUserId()).build());

        log.info("拼团商品查询试算服务-MarketNode userId:{} 异步线程加载数据「GroupBuyActivityDiscountVO、SkuVO」完成", requestParameter.getUserId());
    }

    @Override
    public TrialResponseAggerate doApply(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-MarketNode userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));

        // 获取上下文数据
        ActivityEntity activityEntity = dynamicContext.getActivityEntity();
        DiscountEntity discountEntity = dynamicContext.getDiscountEntity();
        GoodsEntity goodsEntity = dynamicContext.getGoodsEntity();






        // 优惠试算
        IDiscountCalculateService discountCalculateService = discountCalculateServiceMap.get(discountEntity.getMarketPlan());
        if (null == discountCalculateService) {
            log.info("不存在{}类型的折扣计算服务，支持类型为:{}", discountEntity.getMarketPlan(), JSON.toJSONString(discountCalculateServiceMap.keySet()));
            throw new AppException(ResponseCode.E0001.getCode(), ResponseCode.E0001.getInfo());
        }

        // 折扣价格
        BigDecimal payPrice = discountCalculateService.calculate(requestParameter.getUserId(), goodsEntity.getOriginalPrice(), discountEntity,activityEntity);

        goodsEntity.setPayPrice(payPrice);
        goodsEntity.setDeductionPrice(goodsEntity.getOriginalPrice().subtract(payPrice));
        dynamicContext.setGoodsEntity(goodsEntity);


        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<TrialRequestEntity, DefaultActivityStrategyFactory.DynamicContext, TrialResponseAggerate> get(TrialRequestEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 不存在配置的拼团活动，走异常节点
        if (null == dynamicContext.getGoodsEntity() || null == dynamicContext.getDiscountEntity() || null == dynamicContext.getUserEntity() || null == dynamicContext.getActivityEntity()) {

            return errorNode;
        }

        return tagNode;
    }

}
