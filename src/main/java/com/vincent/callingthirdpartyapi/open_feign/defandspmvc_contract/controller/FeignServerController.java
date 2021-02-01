package com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author vincent
 */
@RestController
@RequestMapping(value = "/feign")
@Slf4j
public class FeignServerController {
    private final static String PATH = "/Users/vincent/IDEA_Project/my_project/calling-third-party-api/src/main/java/com/vincent/callingthirdpartyapi/open_feign/defandspmvc_contract";

    @PostMapping(value = "/postPhone")
    public ResponseDto<PhoneDto> postPhone(@RequestBody PhoneQueryDto queryDto, @RequestParam("param") String param) throws JsonProcessingException {
        log.info("postPhone 请求参数 queryDto: {}, param: {}...", queryDto, param);
        if (Objects.isNull(queryDto.getApp())) {
            return ResponseDto.error();
        }
        String jsonString = "{\n" +
                "    \"status\": \"ALREADY_ATT\",\n" +
                "    \"phone\": \"13800138000\",\n" +
                "    \"area\": \"010\",\n" +
                "    \"postno\": \"100000\",\n" +
                "    \"att\": \"中国,北京\",\n" +
                "    \"ctype\": \"北京移动全球通卡\",\n" +
                "    \"par\": \"1380013\",\n" +
                "    \"prefix\": \"138\",\n" +
                "    \"operators\": \"移动\",\n" +
                "    \"style_simcall\": \"中国,北京\",\n" +
                "    \"style_citynm\": \"中华人民共和国,北京市\"\n" +
                "}";
        PhoneDto phoneDto = new ObjectMapper().readValue(jsonString, PhoneDto.class);
        return ResponseDto.success(phoneDto);
    }

    @PostMapping(value = "/postFormTime")
    public ResponseDto<TimeDto> postFormTime(TimeQueryDto queryDto) throws JsonProcessingException {
        log.info("postFormTime 请求参数 queryDto: {}...", queryDto);
        if (Objects.isNull(queryDto.getApp())) {
            return ResponseDto.error();
        }
        String jsonString = "{\n" +
                "    \"timestamp\": \"1611728644\",\n" +
                "    \"datetime_1\": \"2021-01-27 14:24:04\",\n" +
                "    \"datetime_2\": \"2021年01月27日 14时24分04秒\",\n" +
                "    \"week_1\": \"3\",\n" +
                "    \"week_2\": \"星期三\",\n" +
                "    \"week_3\": \"周三\",\n" +
                "    \"week_4\": \"Wednesday\"\n" +
                "}";
        TimeDto timeDto = new ObjectMapper().readValue(jsonString, TimeDto.class);
        return ResponseDto.success(timeDto);
    }

    @GetMapping(value = "/getIDCard")
    public ResponseDto<IDCardDto> getIdCard(@RequestParam("app") String app,
                                            @RequestParam("idcard") String idcard,
                                            @RequestParam("appkey") String appkey,
                                            @RequestParam("sign") String sign,
                                            @RequestParam("format") String format) throws JsonProcessingException {
        log.info("getIDCard 请求参数 app: {}, idcard: {}, appkey: {}, sign: {}, format: {}...", app, idcard, appkey, sign, format);
        if (StringUtils.isBlank(app)) {
            return ResponseDto.error();
        }
        String jsonString = "{\n" +
                "    \"status\": \"ALREADY_ATT\",\n" +
                "    \"idcard\": \"110101199001011114\",\n" +
                "    \"par\": \"110101\",\n" +
                "    \"born\": \"1990年01月01日\",\n" +
                "    \"sex\": \"男\",\n" +
                "    \"att\": \"北京市 东城区 \",\n" +
                "    \"postno\": \"100000\",\n" +
                "    \"areano\": \"010\",\n" +
                "    \"style_simcall\": \"中国,北京\",\n" +
                "    \"style_citynm\": \"中华人民共和国,北京市\"\n" +
                "}";
        IDCardDto idCardDto = new ObjectMapper().readValue(jsonString, IDCardDto.class);
        return ResponseDto.success(idCardDto);
    }

    @GetMapping(value = "/getRestFul/{param}")
    public ResponseDto<String> getRestFul(@PathVariable("param") String param) {
        log.info("getRestFul 请求参数 param: {}...", param);
        if (Objects.isNull(param)) {
            return ResponseDto.error();
        }
        return ResponseDto.success("请求成功, 返回 param: " + param);
    }

    @PostMapping(value = "/upload")
    public ResponseDto<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("upload 请求参数 MultipartFile file: {}...", file);
        String name = file.getOriginalFilename();
        log.info("File name: {}...", name);
        file.transferTo(Paths.get(PATH).resolve("file2.txt"));
        return ResponseDto.success(name + " upload success...");
    }

    @GetMapping(value = "/downloadBySpringMvc")
    public FileSystemResource downloadBySpringMvc(@RequestParam("fileName") String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("downloadBySpringMvc 请求参数 String fileName: {}...", fileName);
        Path filePath = Paths.get(PATH).resolve(fileName);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode("fileName.txt", "UTF-8"));
        return new FileSystemResource(filePath);
    }

    @GetMapping(value = "/downloadByHttpServlet")
    public void downloadByHttpServlet(@RequestParam("fileName") String fileName, HttpServletResponse response) throws Exception {
        log.info("downloadByHttpServlet 请求参数 String fileName: {}...", fileName);
//        Path filePath = Paths.get(PATH).resolve(fileName);
//        response.getOutputStream().write(Files.readAllBytes(filePath));
//        IOUtils.copy(Files.newInputStream(filePath), response.getOutputStream());

        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode("fileName.txt", "UTF-8"));
        response.getOutputStream().write((fileName + "download success...").getBytes(StandardCharsets.UTF_8));
    }
}
