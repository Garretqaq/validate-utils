package main;

import com.dato.Interceptor.MyMethodValidationInterceptor;
import com.dato.service.impl.MyExecutableValidator;
import org.springframework.aop.framework.ProxyFactory;

import java.util.Arrays;

public class Proxy {
    public static void main(String[] args) {
        // 创建被代理对象
        ValidatorTest test = new ValidatorTest();
        // 创建一个ProxyFactory对象
        ProxyFactory factory = new ProxyFactory();
        // 设置目标对象
        factory.setTarget(test);
        // 进行方法调用
        factory.addAdvice(new MyMethodValidationInterceptor(new MyExecutableValidator()));

        ValidatorTest instance = (ValidatorTest) factory.getProxy();

        TestParam testParam = new TestParam();
        testParam.setA("666");
        testParam.setList(Arrays.asList(1, 2, 3, 4, 5));
        instance.test(testParam);
    }
}
