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
    @Bean
    public Feign.Builder doNotCloseAfterDecode() {
        return Feign.builder().doNotCloseAfterDecode();
    }

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

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
