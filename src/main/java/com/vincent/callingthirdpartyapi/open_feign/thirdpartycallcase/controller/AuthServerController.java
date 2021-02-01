package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.controller;

import com.google.common.collect.Lists;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.config.AccessTokenFilter;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.enums.ResultCodeErrorEnum;
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
@RequestMapping(value = "/autho")
@Slf4j
public class AuthServerController {
    private static final String GRANT_TYPE = "client_credentials";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";

    private static final String TOKEN = "token";

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public ResponseDto<AuthServerDto> getAccessToken(@RequestParam("grant_type") String grantType,
                                                     @RequestParam("client_id") String clientId,
                                                     @RequestParam("client_secret") String clientSecret) {
        log.info("getAccessToken 请求参数 grant_type: {}, client_id: {},client_secret: {} ...", grantType, clientId, clientSecret);
        if (!StringUtils.equals(grantType, GRANT_TYPE)) {
            return ResponseDto.error(ResultCodeErrorEnum.AUTH_TYPE_ERROR);
        }
        if (!StringUtils.equals(clientId, CLIENT_ID) && !StringUtils.equals(clientSecret, CLIENT_SECRET)) {
            return ResponseDto.error(ResultCodeErrorEnum.AUTH_ERROR);
        }
        // 获取 token, 并重置 token 的过期时间
        List<String> tokenList = AccessTokenFilter.TOKEN_MAP.get(TOKEN);
        String token = tokenList.get(1);
        tokenList.remove(0);
        tokenList.add(0, String.valueOf(System.currentTimeMillis() + AccessTokenFilter.EXPIRES_IN));

        AuthServerDto authDto = new AuthServerDto();
        authDto.setAccessToken(token);
        authDto.setTokenType("bearer");
        authDto.setExpiresIn(3 * 60 * 1000);
        return ResponseDto.success(authDto);
    }

    @GetMapping(value = "/user/get")
    public ResponseDto<TpUserDto> getUserDto(@RequestParam("accessToken") String accessToken, @RequestParam("userId") String userId) {
        log.info("getUserDto 请求参数 accessToken: {}, userId: {} ...", accessToken, userId);
        TpUserDto userDto = new TpUserDto();
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setUserCode("USERCODE_VINCENT");
        userDto.setUserName("Vincent");
        return ResponseDto.success(userDto);
    }

    @PostMapping(value = "/department/list")
    public ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(@RequestParam("accessToken") String accessToken, @RequestBody TpDepartmentQueryDto queryDto) {
        log.info("getDepartmentDtos 请求参数 accessToken: {}, queryDto: {} ...", accessToken, queryDto);
        return ResponseDto.success(Lists.newArrayList(
                new TpDepartmentDto(19000L, "xxx公司", "xxx_company", 1L, 1L),
                new TpDepartmentDto(19580L, "人事部", "personnel_department", 19000L, 23L),
                new TpDepartmentDto(19581L, "财务部", "finance_department", 19000L, 24L),
                new TpDepartmentDto(19582L, "技术部", "technology_department", 19000L, 25L)
        ));
    }
}
