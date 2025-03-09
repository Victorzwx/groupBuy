package cn.bugstack.domain.activity.model.aggregate;

import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.DiscountEntity;
import cn.bugstack.domain.activity.model.entity.GoodsEntity;
import cn.bugstack.domain.activity.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrialResponseAggerate {
    private ActivityEntity activityEntity;
    private GoodsEntity goodsEntity;
    private DiscountEntity discountEntity;
    private UserEntity userEntity;
}
