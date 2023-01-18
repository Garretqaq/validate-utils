package com.dato.service.convertors.impl;


import cn.hutool.core.convert.Convert;
import com.dato.entity.OrderLog;
import com.dato.service.convertors.Convertors;
import com.dato.test.TestResultVO;


import java.util.Date;

public class OrderLogConvertor implements Convertors<OrderLog> {
    @Override
    public OrderLog convert(Object object) {
        OrderLog orderLog = new OrderLog();
        TestResultVO resultVO = Convert.convert(TestResultVO.class, object);

        orderLog.setPhone(resultVO.getPhone());
        orderLog.setCreateTime(new Date());
        orderLog.setUserName(resultVO.getName());
        return orderLog;
    }
}
