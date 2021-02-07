package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.updownloadfile;

import com.google.common.collect.ImmutableMap;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.config.FeignConfig;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwrapfactory.DefaultFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

/**
 * @author vincent
 */
@FeignClient(name = "UpDownloadFileApiClient", url = "http://localhost:8080/file",
        configuration = FeignConfig.class,
        fallbackFactory = UpDownloadFileApiClient.UpDownloadFileApiClientFactory.class
)
public interface UpDownloadFileApiClient {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseDto<String> upload(@RequestPart("file") MultipartFile file);

    @GetMapping(value = "/download")
    InputStream download(@RequestParam("fileName") String fileName);

    @GetMapping(value = "/download2")
    InputStream download2(@RequestParam("fileName") String fileName);


    @Component
    class UpDownloadFileApiClientFactory implements DefaultFallbackFactory<UpDownloadFileApiClient> {
        private static final Map<Class<?>, Function<Throwable, Object>> WRAPPER_EXCEPTION = ImmutableMap.of(
                // 如果返回类型是 InputStream, 在 http 调用失败的情况下抛出 RuntimeException(e)
                InputStream.class, e -> {
                    throw new RuntimeException(e);
                },
                // 如果返回类型是 ResponseDto, 在 http 调用失败的情况下返回 simpleFailResponseDto(cause)
                ResponseDto.class, DefaultFallbackFactory::simpleFailResponseDto
        );

        @Override
        public Map<Class<?>, Function<Throwable, Object>> wrapperException() {
            return WRAPPER_EXCEPTION;
        }
    }
}
