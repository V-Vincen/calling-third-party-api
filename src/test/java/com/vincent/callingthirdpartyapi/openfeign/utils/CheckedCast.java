package com.vincent.callingthirdpartyapi.openfeign.utils;

/**
 * @author vincent
 * 强制类型转换
 */
public class CheckedCast {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
