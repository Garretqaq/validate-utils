package com.dato.service.convertors;

public interface Convertors<T> {

    /**
     * 转换对象
     * @param object 需要转换的对象
     * @return 转换后对象
     */
    public T convert(Object object);
}
