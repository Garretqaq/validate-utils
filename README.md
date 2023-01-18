## 注解

@NotNull : 对字段进行判空

@Size: 对数组大小进行限制

## 原理

注解校验逻辑是，在方法增加前置切面，获取方法的parameterClass，和executableParameter。也就是参数类对象和参数实例。去判断每个字段上面有没有@NotNull和@Size的父类注解,有的话则把参数信息保存下来，然后使用校验类去校验。如果失败就得到校验错误异常。

## 类介绍

#### MyMethodValidationInterceptor类

```java
 @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        MyExecutableValidator execVal = this.validator.forExecutables();
        // 需要验证的方法
        Method methodToValidate = invocation.getMethod();
        Set<MyConstraintViolation<Object>> result;

        result = execVal.validateParameters(
                invocation.getThis(), methodToValidate, invocation.getArguments());

        // 如果有值则需要抛出异常
        if (!CollectionUtils.isEmpty(result)){
            throw new ValidationException(result); 
        }

        return methodToValidate.invoke(invocation.getThis(), invocation.getArguments());
    }
```

这是切面前的判断逻辑，通过MyExecutableValidator去校验参数获取结果

#### MethodValidationInitializingBean类

```java
public class MethodValidationInitializingBean extends MethodValidationPostProcessor implements InitializingBean{

    @Override
    public void afterPropertiesSet() throws Exception {
        Pointcut pointcut = new AnnotationMatchingPointcut(Validated.class, true);
        super.advisor = new DefaultPointcutAdvisor(pointcut, new MyMethodValidationInterceptor(super.validator));
    }

}
```

主要实现InitializingBean实现在Spring属性注入之后，找到类上面Validated注解的类，advisor = poincut + advice，一个aop得含有切点和切面。这个类就是构造需要代理的信息，以便后续传入ProxyFactory进行代理。

#### MethodValidationPostProcessor 类

```java
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
```

实现BeanPostProcessor，在Spring生命周期对bean对象进行后置处理。MethodValidationPostProcessor对bean进行后置处理，如果这个类符和pointCut的切点逻辑，则会进行代理生成代理对象，从而完成简单的方法上注解校验功能

### 总结

@Validated框架很值得学习，能够很好的提高代码的简洁性和维护性，省去其中很多冗余代码，非常方便的进行参数校验。

其中的校验逻辑，和反射处理，设计模式还是很值得大家去学习的。包括分组校验 ，多级校验。本代码只是浅浅实现一些基本原理，还有很多漏洞，主要学习框架的思想和原理，希望对大家有帮助
