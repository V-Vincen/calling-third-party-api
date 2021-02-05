package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.controller;

import com.google.common.collect.Lists;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.config.AccessTokenFilter;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.enums.ResultCodeErrorEnum;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.utils.TpCallCaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author vincent
 */
@RestController
@RequestMapping(value = "/server")
@Slf4j
public class ServerSimulatorController {
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public AuthServerDto getAccessToken(@RequestParam("grant_type") String grantType,
                                        @RequestParam("client_id") String clientId,
                                        @RequestParam("client_secret") String clientSecret) {
        log.info("GetAccessToken method request parameters -> grant_type: {}, client_id: {},client_secret: {} ...", grantType, clientId, clientSecret);
        // token 的校验，这里就不再赘述了，上一篇文章中已经举了一个简单的例子
        AuthServerDto authDto = new AuthServerDto();
        authDto.setAccessToken(UUID.randomUUID().toString());
        authDto.setTokenType("bearer");
        authDto.setExpiresIn(3 * 60 * 1000);
        return authDto;
    }

    @GetMapping(value = "/user/get")
    public ResponseDto<TpUserDto> getUserDto(@RequestParam("accessToken") String accessToken, @RequestParam("userId") String userId) {
        log.info("GetUserDto method request parameters -> accessToken: [{}], userId: [{}] ...", accessToken, userId);
        TpUserDto userDto = new TpUserDto();
        userDto.setUserId(userId);
        userDto.setUserCode("USERCODE_VINCENT");
        userDto.setUserName("Vincent");
        return ResponseDto.success(userDto);
    }

    @PostMapping(value = "/department/list")
    public ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(@RequestParam("accessToken") String accessToken, @RequestBody TpDepartmentQueryDto queryDto) {
        log.info("GetDepartmentDtos method request parameters -> accessToken: [{}], queryDto: [{}] ...", accessToken, queryDto);
        return ResponseDto.success(Lists.newArrayList(
                new TpDepartmentDto(19000L, "xxx公司", "xxx_company", 1L, queryDto.getPosition(), 1L),
                new TpDepartmentDto(19580L, "人事部", "personnel_department", 19000L, queryDto.getPosition(), 23L),
                new TpDepartmentDto(19581L, "财务部", "finance_department", 19000L, queryDto.getPosition(), 24L),
                new TpDepartmentDto(19582L, "技术部", "technology_department", 19000L, queryDto.getPosition(), 25L)
        ));
    }
}
