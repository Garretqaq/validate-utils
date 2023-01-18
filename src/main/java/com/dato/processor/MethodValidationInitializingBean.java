package com.dato.processor;

import com.dato.Interceptor.MyMethodValidationInterceptor;
import com.dato.annotion.Validated;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;

public class MethodValidationInitializingBean extends MethodValidationPostProcessor implements InitializingBean{

    @Override
    public void afterPropertiesSet() throws Exception {
        Pointcut pointcut = new AnnotationMatchingPointcut(Validated.class, true);
        super.advisor = new DefaultPointcutAdvisor(pointcut, new MyMethodValidationInterceptor(super.validator));
    }

}
