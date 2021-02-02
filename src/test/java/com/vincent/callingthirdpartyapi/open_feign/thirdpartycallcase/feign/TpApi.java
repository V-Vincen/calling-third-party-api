package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.feign;

import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.AuthServerDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
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
public interface TpApi {
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    ResponseDto<AuthServerDto> getAccessToken(@RequestParam("grant_type") String grantType, @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret);

    @GetMapping(value = "/user/get")
    ResponseDto<TpUserDto> getUserDto(@RequestParam("accessToken") String accessToken, @RequestParam("userId") String userId);

    @PostMapping(value = "/department/list")
    ResponseDto<List<TpDepartmentDto>> getDepartmentDtos(@RequestParam("accessToken") String accessToken, @RequestBody TpDepartmentQueryDto queryDto);
}
