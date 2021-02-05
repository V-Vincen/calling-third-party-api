package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwrapfactory;

import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwrapfactory.utils.ClassUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author vincent
 * 熔断器工厂
 */
public interface DefaultFallbackFactory<T> extends FallbackFactory<T> {

    Map<Class<?>, Function<Throwable, Object>> wrapperException();

    static ResponseDto<?> simpleFailResponseDto(Throwable cause) {
        ResponseDto<?> responseDto = new ResponseDto<>();
        responseDto.setStatus(-100);
        responseDto.setMsg(cause.getClass().getName() + ": detailMessage[ " + Optional.ofNullable(cause.getMessage()).orElse("") + " ]");
        return responseDto;
    }

    @Override
    default T create(Throwable cause) {
        return simpleFailClient(cause);
    }

    @SuppressWarnings("unchecked")
    default T simpleFailClient(Throwable cause) {
        Class<Object> clazz = ClassUtils.getGenericByInterface(this.getClass());
        Map<Class<?>, Function<Throwable, Object>> map = Optional.ofNullable(wrapperException()).orElse(Collections.emptyMap());
        // 动态代理 cglib, 这里是动态生成 T 的实现类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((InvocationHandler) (o, method, objects) -> {
            Class<?> returnType = method.getReturnType();
            if (map.containsKey(returnType)) {
                return map.get(returnType).apply(cause);
            }
            return new RuntimeException(cause);
        });
        return (T) enhancer.create();
    }
}
