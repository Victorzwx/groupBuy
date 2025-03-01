package cn.bugstack.domain.activity.service.dicount;

import cn.bugstack.domain.activity.model.valobj.DiscountTypeEnum;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        // 1. 人群标签过滤
        if(DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowdRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if (!isCrowdRange) return originalPrice;
        }
        return validPrice(docalculate(originalPrice, groupBuyDiscount));
    }
    private boolean filterTagId(String userId, String tagId) {
        return true;
    }

    protected abstract BigDecimal docalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

    private BigDecimal validPrice(BigDecimal price) {
        // 判断折扣后金额，最低支付1分钱
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("优惠策略折扣计算{}小于0", price);
            return new BigDecimal("0.01");
        }

        return price;
    }



}
