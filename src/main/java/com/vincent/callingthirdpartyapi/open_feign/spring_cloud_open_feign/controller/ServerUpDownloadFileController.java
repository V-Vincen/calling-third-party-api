package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.controller;

import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author vincent
 */
@RestController
@Slf4j
@RequestMapping(value = "/file")
public class ServerUpDownloadFileController {

    private final static String PATH = "/Users/vincent/IDEA_Project/my_project/calling-third-party-api/src/main/java/com/vincent/callingthirdpartyapi/open_feign/spring_cloud_open_feign";

    @PostMapping(value = "/upload")
    public ResponseDto<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Upload Method Params MultipartFile file: {}...", file);
        String name = file.getOriginalFilename();
        log.info("File name: {}...", name);
        file.transferTo(Paths.get(PATH).resolve("upload_file.txt"));
        return ResponseDto.success(name + " upload success...");
    }

    /**
     * Spring 提供的类：FileSystemResource
     *
     * @param fileName 文件名
     * @param response 响应
     * @return FileSystemResource
     * @throws UnsupportedEncodingException 异常
     */
    @GetMapping(value = "/download")
    public FileSystemResource download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("Download Method Params String fileName: {}...", fileName);
        Path filePath = Paths.get(PATH).resolve(fileName);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode("download_file.txt", StandardCharsets.UTF_8.toString()));
        return new FileSystemResource(filePath);
    }

    @GetMapping(value = "/download2")
    public void download2(@RequestParam("fileName") String fileName, HttpServletResponse response) throws Exception {
        log.info("Download2 Method Params String fileName: {}...", fileName);
        Path filePath = Paths.get(PATH).resolve(fileName);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode("fileName.txt", "UTF-8"));
//        IOUtils.copy(Files.newInputStream(filePath), response.getOutputStream());//这个写法和下面的写法是一个意思
        response.getOutputStream().write(Files.readAllBytes(filePath));
    }
}
