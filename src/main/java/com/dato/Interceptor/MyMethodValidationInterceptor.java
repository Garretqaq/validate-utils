package com.dato.Interceptor;


import cn.hutool.core.annotation.AnnotationUtil;
import com.dato.annotion.AddLog;
import com.dato.entity.OrderLog;
import com.dato.exception.ValidationException;
import com.dato.service.MyValidator;
import com.dato.service.convertors.Convertors;
import com.dato.service.impl.MyExecutableValidator;
import com.dato.service.result.MyConstraintViolation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Set;

public class MyMethodValidationInterceptor implements MethodInterceptor {
    private final MyValidator validator;


//    @Resource
//    private LogMapper logMapper;

    public MyMethodValidationInterceptor(MyValidator validator) {
        this.validator = validator;
    }
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        MyExecutableValidator execVal = this.validator.forExecutables();
        // 需要验证的方法
        Method methodToValidate = invocation.getMethod();
        Set<MyConstraintViolation<Object>> result;

        result = execVal.validateParameters(
                invocation.getThis(), methodToValidate, invocation.getArguments());

        // 如果有值则需要抛出异常
        if (CollectionUtils.isEmpty(result)){
            if (AnnotationUtil.hasAnnotation(methodToValidate, AddLog.class)){
                AddLog annotation = methodToValidate.getDeclaredAnnotation(AddLog.class);
                // 转换成对象
                Convertors<OrderLog> covert = annotation.covert().getDeclaredConstructor().newInstance();
                // 生成插入日志实体
                OrderLog logEntity = covert.convert(methodToValidate.invoke(invocation.getThis(), invocation.getArguments()));
                // 插入日志表
//                int count = logMapper.insert(logEntity);
                System.out.println("插入日志成功, 参数：" + logEntity.toString());
            }
            invocation.proceed();
        }else {
            throw new ValidationException(result);
        }

        return methodToValidate.invoke(invocation.getThis(), invocation.getArguments());
    }



}
