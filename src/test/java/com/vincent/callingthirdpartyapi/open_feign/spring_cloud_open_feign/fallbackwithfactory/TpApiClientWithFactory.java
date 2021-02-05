package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwithfactory;

import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author vincent
 */
@FeignClient(name = "TpApiClientWithFactory", url = "http://localhost:8080/server", fallbackFactory = TpApiClientWithFactory.TpApiClientFallbackFactory.class)
public interface TpApiClientWithFactory {
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    AuthServerDto getAccessToken(@RequestParam("grant_type") String grantType, @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret);

    @GetMapping(value = "/user/get")
    ResponseDto<TpUserDto> getUserDto(@RequestParam("accessToken") String accessToken, @RequestParam("userId") String userId);

    @PostMapping(value = "/department/list")
    ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(@RequestParam("accessToken") String accessToken, @RequestBody TpDepartmentQueryDto queryDto);


    @Component
    class TpApiClientFallbackFactory implements FallbackFactory<TpApiClientWithFactory> {
        @Override
        public TpApiClientWithFactory create(Throwable cause) {
            return new TpApiClientWithFactory() {
                @Override
                public AuthServerDto getAccessToken(String grantType, String clientId, String clientSecret) {
                    throw new RuntimeException(cause);
                }

                @Override
                public ResponseDto<TpUserDto> getUserDto(String accessToken, String userId) {
                    return new ResponseDto<>(-100, cause.getClass().getName() + ": detailMessage[ " + Optional.ofNullable(cause.getMessage()).orElse("") + " ]");
                }

                @Override
                public ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(String accessToken, TpDepartmentQueryDto queryDto) {
                    return new ResponseDto<>(-100, cause.getClass().getName() + ": detailMessage[ " + Optional.ofNullable(cause.getMessage()).orElse("") + " ]");
                }
            };
        }
    }
}
