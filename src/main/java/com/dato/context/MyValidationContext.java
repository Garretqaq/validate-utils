package com.dato.context;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.dato.annotion.Constraint;
import com.dato.annotion.constraint.NotNull;
import com.dato.annotion.constraint.Size;
import com.dato.service.result.MyConstraintViolation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 校验类的一些基本信息
 */
@Getter
@Setter
public class MyValidationContext<T> {

    private final T rootBean;
    /**
     * 需要处理的类
     */
    private final Class<T> rootBeanClass;
    /**
     * 传入的参数
     */
    private final Object[] executableParameters;
    /**
     * 方法
     */
    private final Method beanMethod;
    /**
     * 需要校验的参数
     */
    private List<ValidationMessage> validationMessageList;


    public MyValidationContext(T rootBean, Object[] executableParameters, Method beanMethod) {
        this.rootBean = rootBean;
        this.rootBeanClass = (Class<T>) rootBean.getClass();
        this.executableParameters = executableParameters;
        this.beanMethod = beanMethod;
        this.validationMessageList = new ArrayList<>();
    }


    /**
     * 判断是否需要校验
     * @return 是否校验
     */
    public boolean shouldValidation(){
        Class<?>[] parameterTypes = beanMethod.getParameterTypes();
        if (parameterTypes.length == 0){
            return false;
        }

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Object executableParameter = executableParameters[i];

            // 通过参数类型和实例获取需校验的信息
            List<ValidationMessage> validationMessage = getValidationMessage(parameterType, executableParameter);
            validationMessageList.addAll(validationMessage);
        }

        return CollectionUtil.isNotEmpty(validationMessageList);
    }

    /**
     * 获取需要校验的参数，并返回
     * @param parameterClass 参数类型
     * @param executableParameter 执行方法的参数实例
     * @return 需校验消息
     */
    private static <T> List<ValidationMessage> getValidationMessage(Class<T> parameterClass, Object executableParameter) {
        List<ValidationMessage> list = new ArrayList<>();
        Field[] fields = parameterClass.getDeclaredFields();

        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                // 如果有父类注解, 则保存注解信息到ValidationMessage中
                if (AnnotationUtil.hasAnnotation(field, Constraint.class)){
                    ValidationMessage validationMessage = new ValidationMessage();
                    validationMessage.setParamField(field);
                    validationMessage.setValue(ReflectUtil.getFieldValue(executableParameter, field));
                    validationMessage.setAnnotation(annotation);
                    validationMessage.setParamClass(parameterClass);
                    validationMessage.setType(field.getGenericType());
                    list.add(validationMessage);
                }
            }
        }

        return list;
    }

    /**
     * 在context里面进行校验
     * @return 校验结果
     */
    public Set<MyConstraintViolation<T>> validationInContext() {
        Set<MyConstraintViolation<T>> myConstraintViolation  = new HashSet<>();

        for (ValidationMessage validationMessage : this.validationMessageList) {
            Annotation annotation = validationMessage.getAnnotation();
            if (annotation instanceof NotNull){
                System.out.println("进行@NotNull 判断逻辑");

                // 校验不为空
                MyConstraintViolation<T> result = notNullToCheck(validationMessage);
                if (result != null){
                    myConstraintViolation.add(result);
                }
            }else if (annotation instanceof Size){
                System.out.println("进行@Size 判断逻辑");

                // Size校验逻辑
                MyConstraintViolation<T> result = sizeToCheck(validationMessage);
                if (result != null){
                    myConstraintViolation.add(result);
                }
            }
        }

        return myConstraintViolation;
    }



    private MyConstraintViolation<T> sizeToCheck(ValidationMessage validationMessage) {
        MyConstraintViolation<T> result = new MyConstraintViolation<>();
        Collection<?> collection = (Collection<?>) validationMessage.getValue();

        Size sizeAnnotation = (Size) validationMessage.getAnnotation();
        int max = sizeAnnotation.max();

        if (CollectionUtils.isEmpty(collection)){
            result.setParamField(validationMessage.getParamField());
            result.setParamClass(validationMessage.getParamClass());
            result.setExceptionMessage("值不能为空");
            return result;
        }else if (collection.size() > max){
            result.setParamField(validationMessage.getParamField());
            result.setParamClass(validationMessage.getParamClass());
            result.setExceptionMessage("当前集合大小为" + collection.size() + ",超过最大值限制");
            return result;
        }else {
            return null;
        }

    }


    private MyConstraintViolation<T> notNullToCheck(ValidationMessage validationMessage) {
        MyConstraintViolation<T> result = new MyConstraintViolation<>();

        if (validationMessage.getValue() == null){
            result.setParamField(validationMessage.getParamField());
            result.setParamClass(validationMessage.getParamClass());
            result.setExceptionMessage("值不能为空");
            return result;
        }

        return null;
    }
}
