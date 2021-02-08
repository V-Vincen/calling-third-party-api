package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.config;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class FeignConfig {
    /**
     * doNotCloseAfterDecode(): 该构造方法的作用为，当响应返回的 response 进行解码器解析后，对其不进行关闭。
     * 这里主要用于文件下载时，以流的形式成功返回后，防止流的关闭。
     * 如果不设置该值，在调用第三方的下载接口时，会抛出 java.io.IOException: stream is closed 异常。
     *
     * @return Feign.Builder
     */
    @Bean
    public Feign.Builder doNotCloseAfterDecode() {
        return Feign.builder().doNotCloseAfterDecode();
    }

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * 编码器: 支持以下三种请求格式
     * 1. application/json
     * 2. application/x-www-form-urlencoded
     * 3. multipart/form-data
     *
     * @return Encoder
     */
    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    /**
     * 解码器: 支持两种响应格式
     * 1. 返回值类型为 InputStream（主要用于文件下载）
     * 2. 其余返回值类型，以 json 数据格式进行解析
     *
     * @return Decoder
     */
    @Bean
    public Decoder feignFormDecoder() {
        return (response, type) -> {
            if (type == InputStream.class) {
                return response.body().asInputStream();
            }
            return new JacksonDecoder().decode(response, type);
        };
    }
}
