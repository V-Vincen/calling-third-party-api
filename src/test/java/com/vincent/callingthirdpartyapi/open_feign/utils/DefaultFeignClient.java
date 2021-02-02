package com.vincent.callingthirdpartyapi.open_feign.utils;

import com.google.common.collect.ImmutableMap;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.Request;
import feign.Request.Options;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.Retryer;
import feign.codec.EncodeException;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.HttpMethod;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author vincent
 * openfeign 默认构造器
 */
@Slf4j
public class DefaultFeignClient {
    private final static Map<ContractEnum, Contract> CONTRACT_MAP = ImmutableMap.of(
            ContractEnum.DEFAULT, new Contract.Default(),
            ContractEnum.SPRINGMVC, new SpringMvcContract()
    );

    private final static SSLSocketFactory SSL_SOCKET_FACTORY;

    private final static Map<String, Object> FEIGN_CLIENT_CACHE = new HashMap<>();

    static {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            SSL_SOCKET_FACTORY = sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("Init SSLSocketFactory fail...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Feign 构造器
     *
     * @param apiType            请求目标类
     * @param url                请求路径
     * @param contractEnum       定义接口上有效的注释 {@link Contract},  默认 feign 自带注解 {@link Contract.Default}, 也可使用 {@link SpringMvcContract} 注解
     * @param requestInterceptor 请求拦截器 {@link RequestInterceptor}（在请求前做一些特殊需求处理，需自己实现 {@link RequestInterceptor#apply(RequestTemplate)} 接口方法）
     * @param options            请求可选设置 {@link Request.Options}, 默认设置 {@link Request.Options#Options()}, 自定义设置 {@link Request.Options#Options(long, TimeUnit, long, TimeUnit, boolean)}
     * @param retryer            请求重试设置 {@link Retryer}, 默认设置 {@link Retryer.Default#Default()}, 自定义设置 {@link Retryer.Default#Default(long, long, int)}
     * @param errorDecoder       请求返回值解码器 {@link ErrorDecoder}, 默认设置 {@link ErrorDecoder.Default#decode(String, Response)},
     *                           当 HTTP {@link Response} 的 {@link Response#status()} 值不在 2xx 范围内时，可自定义实现 {@link ErrorDecoder.Default#decode(String, Response)} 接口方法）
     * @param <T>                请求目标类泛型
     * @return 返回 DefaultFeignClient 实例
     */
    private static <T> T createClient(Class<T> apiType, String url, ContractEnum contractEnum,
                                      Consumer<RequestTemplate> requestInterceptor, Options options,
                                      Retryer retryer, BiFunction<String, Response, Exception> errorDecoder) {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(requestInterceptor::accept)
                .contract(CONTRACT_MAP.get(contractEnum))
                .client(new Client.Default(SSL_SOCKET_FACTORY, NoopHostnameVerifier.INSTANCE))
                .options(options)
                .retryer(retryer)
                .encoder(DefaultFeignClient::encode)
                .decoder(DefaultFeignClient::decode)
                .errorDecoder(errorDecoder::apply)
                .target(apiType, url);
    }

    /**
     * 创建 DefaultFeignClient 单例
     *
     * @param apiType            请求目标类
     * @param url                请求路径
     * @param contractEnum       定义接口上有效的注释 {@link Contract},  默认 feign 自带注解 {@link Contract.Default}, 也可使用 {@link SpringMvcContract} 注解
     * @param requestInterceptor 请求拦截器 {@link RequestInterceptor}（在请求前做一些特殊需求处理，需自己实现 {@link RequestInterceptor#apply(RequestTemplate)} 接口方法）
     * @param options            请求可选设置 {@link Request.Options}, 默认设置 {@link Request.Options#Options()}, 自定义设置 {@link Request.Options#Options(long, TimeUnit, long, TimeUnit, boolean)}
     * @param retryer            请求重试设置 {@link Retryer}, 默认设置 {@link Retryer.Default#Default()}, 自定义设置 {@link Retryer.Default#Default(long, long, int)}
     * @param errorDecoder       请求返回值解码器 {@link ErrorDecoder}, 默认设置 {@link ErrorDecoder.Default#decode(String, Response)},
     *                           当 HTTP {@link Response} 的 {@link Response#status()} 值不在 2xx 范围内时，可自定义实现 {@link ErrorDecoder.Default#decode(String, Response)} 接口方法）
     * @param <T>                请求目标类泛型
     * @return 返回 DefaultFeignClient 单例
     */
    public static <T> T getSingleClient(Class<T> apiType, String url, ContractEnum contractEnum,
                                        Consumer<RequestTemplate> requestInterceptor, Options options,
                                        Retryer retryer, BiFunction<String, Response, Exception> errorDecoder) {
        T singletonClient = CheckedCast.cast(FEIGN_CLIENT_CACHE.get(apiType.getName()));
        if (Objects.isNull(singletonClient)) {
            synchronized (FEIGN_CLIENT_CACHE) {
                singletonClient = CheckedCast.cast(FEIGN_CLIENT_CACHE.get(apiType.getName()));
                if (Objects.isNull(singletonClient)) {
                    singletonClient = createClient(apiType, url, contractEnum, requestInterceptor, options, retryer, errorDecoder);
                    FEIGN_CLIENT_CACHE.put(apiType.getName(), singletonClient);
                }
            }
        }
        return singletonClient;
    }

    /**
     * 创建 DefaultFeignClient 单例
     *
     * @param apiType      请求目标类
     * @param url          请求路径
     * @param contractEnum 定义接口上有效的注释 {@link Contract},  默认 feign 自带注解 {@link Contract.Default}, 也可使用 {@link SpringMvcContract} 注解
     * @param <T>          请求目标类泛型
     * @return 返回 DefaultFeignClient 单例
     */
    public static <T> T getSingleClient(Class<T> apiType, String url, ContractEnum contractEnum) {
        return getSingleClient(apiType, url, contractEnum, requestTemplate -> {
        }, new Options(), new Retryer.Default(), (methodKey, response) -> new ErrorDecoder.Default().decode(methodKey, response));
    }

    /**
     * 创建 DefaultFeignClient 单例, 默认使用 springmvc 注解
     *
     * @param apiType 请求目标类
     * @param url     请求路径
     * @param <T>     请求目标类泛型
     * @return 返回 DefaultFeignClient 单例
     */
    public static <T> T getSingleClient(Class<T> apiType, String url) {
        return getSingleClient(apiType, url, ContractEnum.SPRINGMVC);
    }

    /**
     * 创建 DefaultFeignClient 实例
     *
     * @param apiType            请求目标类
     * @param url                请求路径
     * @param contractEnum       定义接口上有效的注释 {@link Contract},  默认 feign 自带注解 {@link Contract.Default}, 也可使用 {@link SpringMvcContract} 注解
     * @param requestInterceptor 请求拦截器 {@link RequestInterceptor}（在请求前做一些特殊需求处理，需自己实现 {@link RequestInterceptor#apply(RequestTemplate)} 接口方法）
     * @param options            请求可选设置 {@link Request.Options}, 默认设置 {@link Request.Options#Options()}, 自定义设置 {@link Request.Options#Options(long, TimeUnit, long, TimeUnit, boolean)}
     * @param retryer            请求重试设置 {@link Retryer}, 默认设置 {@link Retryer.Default#Default()}, 自定义设置 {@link Retryer.Default#Default(long, long, int)}
     * @param errorDecoder       请求返回值解码器 {@link ErrorDecoder}, 默认设置 {@link ErrorDecoder.Default#decode(String, Response)},
     *                           当 HTTP {@link Response} 的 {@link Response#status()} 值不在 2xx 范围内时，可自定义实现 {@link ErrorDecoder.Default#decode(String, Response)} 接口方法）
     * @param <T>                请求目标类泛型
     * @return 返回 DefaultFeignClient 实例
     */
    public static <T> T getClient(Class<T> apiType, String url, ContractEnum contractEnum,
                                  Consumer<RequestTemplate> requestInterceptor, Options options,
                                  Retryer retryer, BiFunction<String, Response, Exception> errorDecoder) {
        return createClient(apiType, url, contractEnum, requestInterceptor, options, retryer, errorDecoder);
    }

    /**
     * 创建 DefaultFeignClient 实例
     *
     * @param apiType      请求目标类
     * @param url          请求路径
     * @param contractEnum 定义接口上有效的注释 {@link Contract},  默认 feign 自带注解 {@link Contract.Default}, 也可使用 {@link SpringMvcContract} 注解
     * @param <T>          请求目标类泛型
     * @return 返回 DefaultFeignClient 实例
     */
    public static <T> T getClient(Class<T> apiType, String url, ContractEnum contractEnum) {
        return getClient(apiType, url, contractEnum, requestTemplate -> {
        }, new Options(), new Retryer.Default(), (methodKey, response) -> new ErrorDecoder.Default().decode(methodKey, response));
    }

    /**
     * 创建 DefaultFeignClient 实例, 默认使用 springmvc 注解
     *
     * @param apiType 请求目标类
     * @param url     请求路径
     * @param <T>     请求目标类泛型
     * @return 返回 DefaultFeignClient 实例
     */
    public static <T> T getClient(Class<T> apiType, String url) {
        return getClient(apiType, url, ContractEnum.SPRINGMVC);
    }

    /**
     * 发送请求时的编码器
     *
     * @param object   请求体中的内容（body 中的内容）
     * @param bodyType 请求体的类型
     * @param template 请求模板
     * @throws EncodeException 编码失败时异常
     */
    private static void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (StringUtils.equalsIgnoreCase(template.method(), HttpMethod.GET.name()) || Objects.isNull(object)) {
            return;
        }
        new SpringFormEncoder(new SpringEncoder(HttpMessageConverters::new)).encode(object, bodyType, template);
    }

    /**
     * 请求响应后进行解密处理
     *
     * @param response 请求响应
     * @param type     接口方法定义的返回类型
     * @return 返回 object（就是 type 的类型）
     * @throws IOException    IO 异常
     * @throws FeignException Feign 异常
     */
    private static Object decode(Response response, Type type) throws IOException, FeignException {
        if (type == InputStream.class) {
            return response.body().asInputStream();
        }
        return new JacksonDecoder().decode(response, type);
    }
}
