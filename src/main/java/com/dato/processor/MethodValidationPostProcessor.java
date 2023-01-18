package com.dato.processor;

import com.dato.service.MyValidator;
import com.dato.service.impl.MyExecutableValidator;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyProcessorSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodValidationPostProcessor extends ProxyProcessorSupport implements BeanPostProcessor {

    protected Advisor advisor;

    /**
     * 校验者
     */
    @Nullable
    protected final MyValidator validator = new MyExecutableValidator();
    /**
     * 缓存该类是否满足代理逻辑
     */
    private final Map<Class<?>, Boolean> eligibleBeans = new ConcurrentHashMap<>(256);



    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (this.advisor == null) {
            return bean;
        }

        // 如果符合则进行代理
        if (isEligible(bean.getClass())) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.addAdvisor(this.advisor);
            proxyFactory.setTarget(bean);
            proxyFactory.setProxyTargetClass(true);
            return proxyFactory.getProxy(getProxyClassLoader());
        }

        return bean;
    }

    /**
     * 判断该类是否需要代理
     * @param targetClass 目标类
     * @return boolean
     */
    protected boolean isEligible(Class<?> targetClass) {
        Boolean eligible = this.eligibleBeans.get(targetClass);
        if (eligible != null) {
            return eligible;
        }
        if (this.advisor == null) {
            return false;
        }
        eligible = AopUtils.canApply(this.advisor, targetClass);
        this.eligibleBeans.put(targetClass, eligible);
        return eligible;
    }
}