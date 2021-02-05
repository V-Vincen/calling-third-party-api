package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.callingthirdpartyapi.CallingThirdPartyApiApplicationTests;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallback.TpApiClient;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwithfactory.TpApiClientWithFactory;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallbackwrapfactory.TpApiClientWrapFactory;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = CallingThirdPartyApiApplicationTests.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        value = {"feign.circuitbreaker.enabled=true"})
@Slf4j
public class CloudFeignFallbackTest {

    @Qualifier("com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.fallback.TpApiClient")
    @Autowired
    private TpApiClient tpApiClient;

    @Autowired
    private TpApiClientWithFactory tpApiClientWithFactory;

    @Autowired
    private TpApiClientWrapFactory tpApiClientWrapFactory;

    @Test
    public void tpApiClientTest() throws JsonProcessingException {
        AuthServerDto accessToken = tpApiClient.getAccessToken("client_credentials", "client_id", "client_secret");
        log.info("GetAccessToken Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(accessToken));

        ResponseDto<TpUserDto> tpUserDtos = tpApiClient.getUserDto(UUID.randomUUID().toString(), "userId");
        log.info("GetUserDto Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tpUserDtos));

        TpDepartmentQueryDto queryDto = new TpDepartmentQueryDto();
        queryDto.setIdOrParentId(19000L);
        queryDto.setPosition("员工");
        ResponseDto<List<TpDepartmentDto>> departmentDtos = tpApiClient.getDepartmentDtos(UUID.randomUUID().toString(), queryDto);
        log.info("GetDepartmentDtos Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(departmentDtos));
    }


    @Test
    public void tpApiClientWrapFactoryTest() throws JsonProcessingException {
//        AuthServerDto accessToken = tpApiClientWithFactory.getAccessToken("client_credentials", "client_id", "client_secret");
//        log.info("GetAccessToken Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(accessToken));

        ResponseDto<TpUserDto> tpUserDtos = tpApiClientWithFactory.getUserDto(UUID.randomUUID().toString(), "userId");
        log.info("GetUserDto Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tpUserDtos));

        TpDepartmentQueryDto queryDto = new TpDepartmentQueryDto();
        queryDto.setIdOrParentId(19000L);
        queryDto.setPosition("员工");
        ResponseDto<List<TpDepartmentDto>> departmentDtos = tpApiClientWithFactory.getDepartmentDtos(UUID.randomUUID().toString(), queryDto);
        log.info("GetDepartmentDtos Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(departmentDtos));
    }

    @Test
    public void tpApiClientWithFactoryTest() throws JsonProcessingException {
        AuthServerDto accessToken = tpApiClientWrapFactory.getAccessToken("client_credentials", "client_id", "client_secret");
        log.info("GetAccessToken Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(accessToken));

        ResponseDto<TpUserDto> tpUserDtos = tpApiClientWrapFactory.getUserDto(UUID.randomUUID().toString(), "userId");
        log.info("GetUserDto Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tpUserDtos));

        TpDepartmentQueryDto queryDto = new TpDepartmentQueryDto();
        queryDto.setIdOrParentId(19000L);
        queryDto.setPosition("员工");
        ResponseDto<List<TpDepartmentDto>> departmentDtos = tpApiClientWrapFactory.getDepartmentDtos(UUID.randomUUID().toString(), queryDto);
        log.info("GetDepartmentDtos Method: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(departmentDtos));
    }
}
