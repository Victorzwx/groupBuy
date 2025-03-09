package cn.bugstack.domain.activity.model.aggregate;


import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.DiscountEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGoodsAggerate {
    private ActivityEntity activityEntity;

    private DiscountEntity discountEntity;

}
