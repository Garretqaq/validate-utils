package com.dato.service.impl;

import cn.hutool.core.lang.Assert;
import com.dato.context.MyValidationContext;
import com.dato.service.MyValidator;
import com.dato.service.result.MyConstraintViolation;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 可执行校验类
 */
public class MyExecutableValidator implements MyValidator {


    @Override
    public MyExecutableValidator forExecutables() {
        return new MyExecutableValidator();
    }

    @Override
    public <T> Set<MyConstraintViolation<T>> validateParameters(T object, Method method, Object[] parameterValues) {
        Assert.notNull(object, "校验对象不能为空");
        Assert.notNull(method, "方法不能为空");
        Assert.notNull(parameterValues, "校验方法的参数不能为空");

        MyValidationContext<T> myValidationContext = new MyValidationContext<>(object, parameterValues, method);
        boolean flag = myValidationContext.shouldValidation();
        if (!flag){
            return null;
        }

        // 在context里面进行校验
        return myValidationContext.validationInContext();
    }
}
