package com.dato.service.result;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * 校验结果类
 * @param <T> 泛型
 */
@Getter
@Setter
public class MyConstraintViolation<T> {
    /**
     * 校验参数类
     */
    private Class<?> paramClass;

    /**
     * 校验字段
     */
    private Field paramField;
    /**
     * 错误消息
     */
    private String exceptionMessage;
}
