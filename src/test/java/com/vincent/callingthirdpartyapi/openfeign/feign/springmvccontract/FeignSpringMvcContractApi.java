package com.vincent.callingthirdpartyapi.openfeign.feign.springmvccontract;

import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.feign.dto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author vincent
 */
public interface FeignSpringMvcContractApi {

    @PostMapping(value = "/postPhone")
    ResponseDto<PhoneDto> postPhone(@RequestBody PhoneQueryDto queryDto, @RequestParam("param") String param);

    @PostMapping(value = "/postFormTime", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseDto<TimeDto> postFormTime(TimeQueryDto queryDto);

    @GetMapping(value = "/getIDCard")
    ResponseDto<IDCardDto> getIdCard(@RequestParam("app") String app,
                                     @RequestParam("idcard") String idcard,
                                     @RequestParam("appkey") String appkey,
                                     @RequestParam("sign") String sign,
                                     @RequestParam("format") String format);

    @GetMapping(value = "/getRestFul/{param}")
    ResponseDto<String> getRestFul(@PathVariable("param") String param);

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseDto<String> upload(@RequestPart("file") MultipartFile file);

    @GetMapping(value = "/downloadBySpringMvc")
    InputStream downloadBySpringMvc(@RequestParam("fileName") String fileName);

    @GetMapping(value = "/downloadByHttpServlet")
    InputStream downloadByHttpServlet(@RequestParam("fileName") String fileName);
}
