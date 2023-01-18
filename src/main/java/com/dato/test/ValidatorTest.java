package com.dato.test;

import com.dato.annotion.AddLog;
import com.dato.annotion.Validated;
import com.dato.service.convertors.impl.OrderLogConvertor;
import org.springframework.stereotype.Component;

@Validated
@Component
public class ValidatorTest {
    @AddLog(covert = OrderLogConvertor.class)
    public TestResultVO test(TestParam testParam){
        System.out.println("测试校验功能");
        return TestResultVO.builder()
                .company("广州比地数据科技有限公司")
                .phone(17521268941L)
                .name("宋广智")
                .build();
    }

}
