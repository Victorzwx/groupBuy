package cn.bugstack.test.trigger;

import cn.bugstack.api.IDCCService;
import cn.bugstack.domain.activity.model.aggregate.TrialResponseAggerate;
import cn.bugstack.domain.activity.model.entity.TrialRequestEntity;
import cn.bugstack.domain.activity.service.IIndexGroupBuyMarketService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 动态配置管理测试
 * @create 2025-01-03 19:43
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DCCControllerTest {

    @Resource
    private IDCCService dccService;

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @Test
    public void test_updateConfig() {
        // 动态调整配置
        dccService.updateConfig("downgradeSwitch", "0");
    }

    @Test
    public void test_updateConfig2indexMarketTrial() throws Exception {
        // 动态调整配置
        dccService.updateConfig("downgradeSwitch", "1");
        // 超时等待异步
        Thread.sleep(1000);

        // 营销验证
        TrialRequestEntity trialRequestEntity = new TrialRequestEntity();
        trialRequestEntity.setUserId("xiaofuge");
        trialRequestEntity.setSource("s01");
        trialRequestEntity.setChannel("c01");
        trialRequestEntity.setGoodsId("9890001");

        TrialResponseAggerate trialResponseAggerate = indexGroupBuyMarketService.indexMarketTrial(trialRequestEntity);
        log.info("请求参数:{}", JSON.toJSONString(trialRequestEntity));
        log.info("返回结果:{}", JSON.toJSONString(trialResponseAggerate));
    }


}
