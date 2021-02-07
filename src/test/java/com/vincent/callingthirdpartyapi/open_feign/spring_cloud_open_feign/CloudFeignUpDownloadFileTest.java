package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.callingthirdpartyapi.CallingThirdPartyApiApplicationTests;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.updownloadfile.UpDownloadFileApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(classes = CallingThirdPartyApiApplicationTests.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        value = {"feign.circuitbreaker.enabled=true"}
)
@Slf4j
public class CloudFeignUpDownloadFileTest {
    private final static String PATH = "/Users/vincent/IDEA_Project/my_project/calling-third-party-api/src/test/java/com/vincent/callingthirdpartyapi/open_feign/spring_cloud_open_feign";

    @Autowired
    private UpDownloadFileApiClient upDownloadFileApiClient;

    @Test
    public void uploadTest() throws IOException {
        String name = "upload_test_file.txt";
        Path filePath = Paths.get(PATH).resolve(name);
        String originalFileName = "upload_test_file.txt";
        String contentType = "application/octet-stream";
        byte[] content = Files.readAllBytes(filePath);
        MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);
        ResponseDto<String> responseDto = upDownloadFileApiClient.upload(multipartFile);
        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }

    @Test
    public void downloadTest() throws IOException {
        Path path = Paths.get(PATH);
        InputStream inputStream = upDownloadFileApiClient.download("upload_file.txt");
        IOUtils.copy(inputStream, Files.newOutputStream(path.resolve("down_file.txt")));

        InputStream inputStream2 = upDownloadFileApiClient.download("upload_file.txt");
        IOUtils.copy(inputStream2, Files.newOutputStream(path.resolve("down_file2.txt")));
    }
}
