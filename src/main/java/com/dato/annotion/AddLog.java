package com.dato.annotion;

import com.dato.entity.OrderLog;
import com.dato.service.convertors.Convertors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddLog {

    Class<? extends Convertors<OrderLog>> covert();
}