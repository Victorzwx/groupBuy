package cn.bugstack.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Test
    public void test(ApplicationContext applicationContext) {

        String applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");

        log.info("测试完成");
    }

}
