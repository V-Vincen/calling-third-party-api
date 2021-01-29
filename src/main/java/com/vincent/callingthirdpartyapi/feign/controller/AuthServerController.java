package com.vincent.callingthirdpartyapi.feign.controller;

import com.google.common.collect.Lists;
import com.vincent.callingthirdpartyapi.feign.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.feign.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.feign.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.feign.dto.TpUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vincent
 */
@RestController
@RequestMapping(value = "/autho")
@Slf4j
public class AuthServerController {
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public AuthServerDto getAccessToken(@RequestParam("grant_type") String grantType,
                                        @RequestParam("client_id") String clientId,
                                        @RequestParam("client_secret") String clientSecret) {
        log.info("grant_type: {}, client_id: {},client_secret: {} ...", grantType, clientId, clientSecret);
        AuthServerDto authDto = new AuthServerDto();
        authDto.setAccessToken(UUID.randomUUID().toString());
        authDto.setTokenType("bearer");
        authDto.setExpiresIn(10 * 60);
        return authDto;
    }

    @GetMapping(value = "/user/get")
    public TpUserDto getUserDto(@RequestParam("accessToken") String accessToken, @RequestParam("userId") String userId) {
        log.info("accessToken: {}, userId: {} ...", accessToken, userId);
        TpUserDto userDto = new TpUserDto();
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setUserCode("USERCODE_VINCENT");
        userDto.setUserName("Vincent");
        return userDto;
    }

    @PostMapping(value = "/department/list")
    public List<TpDepartmentDto> getDepartmentDtos(@RequestBody TpDepartmentQueryDto queryDto) {
        log.info("queryDto: {} ...", queryDto);
        return Lists.newArrayList(
                new TpDepartmentDto(19000L, "xxx公司", "xxx_company", 1L, 1L),
                new TpDepartmentDto(19580L, "人事部", "personnel_department", 19000L, 23L),
                new TpDepartmentDto(19581L, "财务部", "finance_department", 19000L, 24L),
                new TpDepartmentDto(19582L, "技术部", "technology_department", 19000L, 25L)
        );
    }
}
