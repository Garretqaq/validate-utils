package com.dato.context;


import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 需要校验的参数信息
 */
@Getter
@Setter
public class ValidationMessage {

    /**
     * 注解类型
     */
    private Annotation annotation;

    /**
     * 参数值
     */
    private Object value;

    /**
     * 参数类
     */
    private Class<?> paramClass;

    /**
     * 字段
     */
    private Field paramField;

    /**
     * 类型
     */
    private Type type;

}
