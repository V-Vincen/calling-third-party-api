package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwrapfactory;

import com.google.common.collect.ImmutableMap;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwrapfactory.TpApiClientWrapFactory.TpApiClientFallbackFactory;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author vincent
 */
@FeignClient(name = "TpApiClientWrapFactory", url = "http://localhost:8080/server", fallbackFactory = TpApiClientFallbackFactory.class)
public interface TpApiClientWrapFactory {
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    AuthServerDto getAccessToken(@RequestParam("grant_type") String grantType, @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret);

    @GetMapping(value = "/user/get")
    ResponseDto<TpUserDto> getUserDto(@RequestParam("accessToken") String accessToken, @RequestParam("userId") String userId);

    @PostMapping(value = "/department/list")
    ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(@RequestParam("accessToken") String accessToken, @RequestBody TpDepartmentQueryDto queryDto);


    @Component
    class TpApiClientFallbackFactory implements DefaultFallbackFactory<TpApiClientWrapFactory> {

        private static final Map<Class<?>, Function<Throwable, Object>> WRAPPER_EXCEPTION = ImmutableMap.of(
                // 如果返回类型是 AuthServerDto, 在 http 调用失败的情况下抛出 RuntimeException(e)
                AuthServerDto.class, e -> {
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
