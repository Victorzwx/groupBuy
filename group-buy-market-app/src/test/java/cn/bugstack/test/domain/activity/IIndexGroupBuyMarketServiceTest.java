package cn.bugstack.test.domain.activity;

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
 * @description 首页营销服务接口测试
 * @create 2024-12-21 11:08
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class IIndexGroupBuyMarketServiceTest {

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;
    @Resource
    private IDCCService dccService;

    /**
     * 测试人群标签功能的时候，可以进入 ITagServiceTest#test_tag_job 执行人群写入
     */
    @Test
    public void test_indexMarketTrial() throws Exception {
        // 动态调整配置
        //dccService.updateConfig("downgradeSwitch", "0");


        TrialRequestEntity trialRequestEntity = new TrialRequestEntity();
        trialRequestEntity.setUserId("xiaofuge");
        trialRequestEntity.setSource("s01");
        trialRequestEntity.setChannel("c01");
        trialRequestEntity.setGoodsId("9890001");

        TrialResponseAggerate trialResponseAggerate = indexGroupBuyMarketService.indexMarketTrial(trialRequestEntity);
        log.info("请求参数:{}", JSON.toJSONString(trialRequestEntity));
        log.info("返回结果:{}", JSON.toJSONString(trialResponseAggerate));
    }

    @Test
    public void test_indexMarketTrial_no_tag() throws Exception {
        TrialRequestEntity trialRequestEntity = new TrialRequestEntity();
        trialRequestEntity.setUserId("dacihua");
        trialRequestEntity.setSource("s01");
        trialRequestEntity.setChannel("c01");
        trialRequestEntity.setGoodsId("9890001");

        TrialResponseAggerate trialResponseAggerate = indexGroupBuyMarketService.indexMarketTrial(trialRequestEntity);
        log.info("请求参数:{}", JSON.toJSONString(trialRequestEntity));
        log.info("返回结果:{}", JSON.toJSONString(trialResponseAggerate));
    }

    @Test
    public void test_indexMarketTrial_error() throws Exception {
        TrialRequestEntity trialRequestEntity = new TrialRequestEntity();
        trialRequestEntity.setUserId("xiaofuge");
        trialRequestEntity.setSource("s01");
        trialRequestEntity.setChannel("c01");
        trialRequestEntity.setGoodsId("9890002");

        TrialResponseAggerate trialResponseAggerate = indexGroupBuyMarketService.indexMarketTrial(trialRequestEntity);
        log.info("请求参数:{}", JSON.toJSONString(trialRequestEntity));
        log.info("返回结果:{}", JSON.toJSONString(trialResponseAggerate));
    }

}
