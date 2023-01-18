package com.dato.service;


import com.dato.service.impl.MyExecutableValidator;
import com.dato.service.result.MyConstraintViolation;

import java.lang.reflect.Method;
import java.util.Set;

public interface MyValidator {
    /**
     * 获取可执行类
     * @return 类
     */
    MyExecutableValidator forExecutables();

    /**
     * 验证对给定方法的参数设置的所有约束。
     *
     * @param <T> 要验证的方法的类型
     * @param object 调用要验证的方法对象
     * @param method 验证参数约束的方法
     * @param parameterValues 调用给定方法里面的参数值
     * @return 具有验证错误冲突的集合
     * @throws IllegalArgumentException 对参数不匹配则抛出的异常
     */
    <T> Set<MyConstraintViolation<T>> validateParameters(T object,
                                                         Method method,
                                                         Object[] parameterValues);
}
