package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallback;

import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author vincent
 */
@FeignClient(name = "tpApiClient", url = "http://localhost:8080/server", fallback = TpApiClient.TpApiClientFallback.class)
public interface TpApiClient {
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    AuthServerDto getAccessToken(@RequestParam("grant_type") String grantType, @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret);

    @GetMapping(value = "/user/get")
    ResponseDto<TpUserDto> getUserDto(@RequestParam("accessToken") String accessToken, @RequestParam("userId") String userId);

    @PostMapping(value = "/department/list")
    ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(@RequestParam("accessToken") String accessToken, @RequestBody TpDepartmentQueryDto queryDto);

    /**
     * @author vincent
     * 熔断器
     */
    @Slf4j
    @Component
    class TpApiClientFallback implements TpApiClient {
        @Override
        public AuthServerDto getAccessToken(String grantType, String clientId, String clientSecret) {
            log.error("Did not get to token...");
            throw new NoFallbackAvailableException("Boom!", new RuntimeException("Did not get to token..."));
        }

        @Override
        public ResponseDto<TpUserDto> getUserDto(String accessToken, String userId) {
            log.error("Not Find TpUserDto...");
            return ResponseDto.error("Not Find TpUserDto...");
        }

        @Override
        public ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(String accessToken, TpDepartmentQueryDto queryDto) {
            log.error("Not Find TpDepartmentDtos...");
            return ResponseDto.error("Not Find TpDepartmentDtos...");
        }
    }
}
