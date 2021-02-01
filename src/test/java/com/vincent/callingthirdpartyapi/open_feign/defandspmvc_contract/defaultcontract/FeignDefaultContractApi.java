package com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.defaultcontract;

import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.IDCardDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.PhoneDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.PhoneQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.TimeDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.TimeQueryDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.io.File;
import java.io.InputStream;

/**
 * @author vincent
 */
public interface FeignDefaultContractApi {

    @RequestLine("POST /postPhone?param={param}")
    ResponseDto<PhoneDto> postPhone(PhoneQueryDto queryDto, @Param("param") String param);

    @RequestLine("POST /postFormTime")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ResponseDto<TimeDto> postFormTime(TimeQueryDto queryDto);

    @RequestLine("GET /getIDCard?app={app}&idcard={idcard}&appkey={appkey}&sign={sign}&format={format}")
    ResponseDto<IDCardDto> getIdCard(@Param("app") String app,
                                     @Param("idcard") String idcard,
                                     @Param("appkey") String appkey,
                                     @Param("sign") String sign,
                                     @Param("format") String format);

    @RequestLine("GET /getRestFul/{param}")
    ResponseDto<String> getRestFul(@Param("param") String param);

    @RequestLine("POST /upload")
    @Headers("Content-Type: multipart/form-data")
    ResponseDto<String> upload(@Param("file") File file);

    @RequestLine("GET /downloadBySpringMvc?fileName={fileName}")
    InputStream downloadBySpringMvc(@Param("fileName") String fileName);

    @RequestLine("GET /downloadByHttpServlet?fileName={fileName}")
    InputStream downloadByHttpServlet(@Param("fileName") String fileName);
}
