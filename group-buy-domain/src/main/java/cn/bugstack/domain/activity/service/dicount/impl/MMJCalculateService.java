package cn.bugstack.domain.activity.service.dicount.impl;

import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.service.dicount.AbstractDiscountCalculateService;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Slf4j
@Service("MMJ")
public class MMJCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal docalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountName());

        String marketExpr = groupBuyDiscount.getMarketExpr();
        String[] split = marketExpr.split(Constants.SPLIT);
        BigDecimal x = new BigDecimal(split[0].trim());
        BigDecimal y = new BigDecimal(split[1].trim());
        //如果没有达到满减金额
        if (originalPrice.compareTo(x) < 0) {
            return originalPrice;
        }
        //原价除以满减金额得到有几个每满减,该数向下取整
        //添加0，使其小数位为0
        BigDecimal num = originalPrice.divide(x, 0,BigDecimal.ROUND_DOWN);


        //满减
        return originalPrice.subtract(y.multiply(num));
    }
}
