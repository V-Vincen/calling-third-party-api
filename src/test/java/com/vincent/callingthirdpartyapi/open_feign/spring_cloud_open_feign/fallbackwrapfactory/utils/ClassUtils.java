package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwrapfactory.utils;


import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author vincent
 */
public class ClassUtils {
    public static <T> Class<T> getGenericBySuperClass(Class<?> clazz) {
        // getGenericSuperclass: 获取父类的泛型
        Type genericSuperclass = clazz.getGenericSuperclass();
        return getGenericType(genericSuperclass);
    }

    public static <T> Class<T> getGenericByInterface(Class<?> clazz) {
        /*
         * getGenericInterfaces: 获取父接口的泛型
         * 例: 父接口 -> interface A<T>
         *     子实现类 -> class B implements A<T>
         *     Class<B> clazz = B.class
         *     clazz.getGenericInterfaces(): 获取 A 接口的泛型（因为接口是多实现的, 所以该方法返回的是 Type[] ）
         */
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (ArrayUtils.isEmpty(genericInterfaces)) {
            return null;
        }

        Type genericInterface = genericInterfaces[0];
        return getGenericType(genericInterface);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getGenericType(Type genericSuperclass) {
        // ParameterizedType 是一个接口，这个类可以用来检验泛型是否被参数化
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return null;
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        //  .getActualTypeArguments(): 获取这个泛型, 实例化后的具体类型
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (ArrayUtils.isEmpty(actualTypeArguments)) {
            return null;
        }

        Type actualTypeArgument = actualTypeArguments[0];
        if (!(actualTypeArgument instanceof Class)) {
            return null;
        }
        return (Class<T>) actualTypeArgument;
    }
}
