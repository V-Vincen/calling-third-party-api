package com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.defaultcontract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.IDCardDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.PhoneDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.PhoneQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.TimeDto;
import com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto.TimeQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.utils.ContractEnum;
import com.vincent.callingthirdpartyapi.open_feign.utils.DefaultFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FeignDefaultContractTest {

    private final static String PATH = "/Users/vincent/IDEA_Project/my_project/calling-third-party-api/src/test/java/com/vincent/callingthirdpartyapi/open_feign/defandspmvc_contract";

    private final static String URL = "http://localhost:8080/feign";

    private static FeignDefaultContractApi singleClient;

    @BeforeAll
    public static void createFeignClint() {
        singleClient = DefaultFeignClient.getSingleClient(FeignDefaultContractApi.class, URL, ContractEnum.DEFAULT);
    }

    @Test
    public void postTest() throws JsonProcessingException {
        PhoneQueryDto phoneQueryDto = new PhoneQueryDto();
        phoneQueryDto.setApp("phone.get");
        phoneQueryDto.setPhone("15021074475");
        phoneQueryDto.setAppkey("10003");
        phoneQueryDto.setSign("b59bc3ef6191eb9f747dd4e83c99f2a4");
        phoneQueryDto.setFormat("json");
        ResponseDto<PhoneDto> responseDto = singleClient.postPhone(phoneQueryDto, "phone");
        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }

    @Test
    public void postFormTest() throws JsonProcessingException {
        TimeQueryDto timeQueryDto = new TimeQueryDto();
        timeQueryDto.setApp("life.time");
        timeQueryDto.setAppkey("10003");
        timeQueryDto.setSign("b59bc3ef6191eb9f747dd4e83c99f2a4");
        timeQueryDto.setFormat("json");
        ResponseDto<TimeDto> responseDto = singleClient.postFormTime(timeQueryDto);
        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }

    @Test
    public void getTest() throws JsonProcessingException {
        ResponseDto<IDCardDto> responseDto = singleClient.getIdCard("idcard.get", "110101199001011114", "appkey", "sign", "json");
        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }

    @Test
    public void getRestFulTest() throws JsonProcessingException {
        ResponseDto<String> responseDto = singleClient.getRestFul("restful");
        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }

    @Test
    public void uploadTest() throws IOException {
        String name = "file.txt";
        Path filePath = Paths.get(PATH).resolve(name);
        ResponseDto<String> responseDto = singleClient.upload(filePath.toFile());
        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }

    @Test
    public void downloadTest() throws IOException {
        Path path = Paths.get(PATH);
        InputStream inSpring = singleClient.downloadBySpringMvc("file.txt");
        IOUtils.copy(inSpring, Files.newOutputStream(path.resolve("downbyspring.txt")));

        InputStream inServlet = singleClient.downloadByHttpServlet("file.txt");
        IOUtils.copy(inServlet, Files.newOutputStream(path.resolve("downbyservlet.txt")));
    }
}
