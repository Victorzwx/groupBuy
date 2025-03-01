package cn.bugstack.domain.activity.service.dicount.impl;

import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.service.dicount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Slf4j
@Service("WHD")
public class WHDCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal docalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("不参与活动");

        return originalPrice;
    }
}
