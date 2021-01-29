package com.vincent.callingthirdpartyapi.resttemplate;

import com.google.common.collect.Maps;
import com.vincent.callingthirdpartyapi.CallingThirdPartyApiApplicationTests;
import com.vincent.callingthirdpartyapi.resttemplate.dto.Product;
import io.vavr.CheckedConsumer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author vincent
 */
@SpringBootTest(classes = CallingThirdPartyApiApplicationTests.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class RestTemplateTest {

    private static final String URL = "http://localhost:8080/product";

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void webClientTest() throws InterruptedException {
        WebClient webClient = WebClient.create(URL);
        webClient.get()
                .uri("/getProductByNoParams")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(CheckedConsumer.of(s -> {
                            TimeUnit.SECONDS.sleep(3);
                            System.out.println(String.format("webClient 发出异步 http 请求，并且开始睡三秒。获取异步 http 请求后的返回值：%s", s));
                        }).unchecked()
                );
        System.out.println("主线程阻塞开始睡 8 秒：======================================================");
        TimeUnit.SECONDS.sleep(8);
    }

    @Test
    void getProductByNoParams() {
        String url = URL + "/getProductByNoParams";

        //方式一：GET 方式获取 JSON 串数据
        String strProduct = restTemplate.getForObject(url, String.class);
        System.out.println("strProduct:" + strProduct);

        //方式二：GET 方式获取 JSON 数据映射后的 Product 实体对象
        Product product = restTemplate.getForObject(url, Product.class);
        System.out.println("product:" + product);

        //方式三：GET 方式获取包含 Product 实体对象 的响应实体 ResponseEntity 对象，用 getBody() 获取
        ResponseEntity<Product> productEntity = restTemplate.getForEntity(url, Product.class);
        System.out.println("productEntity:" + productEntity);
        Product productEntityBody = productEntity.getBody();
        System.out.println("productEntityBody:" + productEntityBody);

        //方式四：1.构建请求实体 HttpEntity 对象，用于配置 Header 信息和请求参数
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity requestEntity = new HttpEntity<>(header);
        //方式一：2.执行请求获取包含 Product 实体对象的响应实体 ResponseEntity 对象，用 getBody() 获取
        ResponseEntity<Product> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Product.class);
        System.out.println("exchange:" + exchange);

        //方式五：根据 RequestCallback 接口实现类设置Header信息，用 ResponseExtractor 接口实现类读取响应数据
        String execute = restTemplate.execute(url, HttpMethod.GET,
                clientHttpRequest -> clientHttpRequest.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE),
                clientHttpResponse -> IOUtils.toString(clientHttpResponse.getBody(), StandardCharsets.UTF_8));
        System.out.println("execute:" + execute);
    }

    @Test
    void getProductById() {
        String url = URL + "/getProductById?id={id}";

        //方式一：将参数的值存在可变长度参数里，按照顺序进行参数匹配
        ResponseEntity<Product> productEntity = restTemplate.getForEntity(url, Product.class, 101);
        System.out.println("productEntity" + productEntity);
        Product productEntityBody = productEntity.getBody();
        System.out.println("productEntityBody:" + productEntityBody);

        //方式二：将请求参数以键值对形式存储到 Map 集合中，用于请求时 URL 上的拼接
        Map<String, Object> uriVariables = Maps.newHashMap();
        uriVariables.put("id", 109);
        Product product = restTemplate.getForObject(url, Product.class, uriVariables);
        System.out.println("product" + product);
    }

    @Test
    void postProductByForm() {
        String url = URL + "/postProductByForm";
        Product product = new Product(201, "MacBook Pro", BigDecimal.valueOf(2148.99), new Date());
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        // 设置请求的 Content-Type 为：application/x-www-form-urlencoded
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        //方式一： 将请求参数以键值对形式存储在 MultiValueMap 集合，发送请求时使用
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("id", product.getId());
        map.add("name", product.getName());
        map.add("price", product.getPrice());
        map.add("date", product.getDate());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, header);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("exchange:" + exchange);

        //方式二： 将请求参数值以 K=V 方式用 & 拼接，发送请求使用
        String productStr = "id=" + product.getId() + "&name=" + product.getName() + "&price=" + product.getPrice() + "&date=" + product.getDate();
        HttpEntity request2 = new HttpEntity<>(productStr, header);
        ResponseEntity<String> exchange2 = restTemplate.exchange(url, HttpMethod.POST, request2, String.class);
        System.out.println("exchange2: " + exchange2);
    }

    @Test
    void postProductByObject() {
        String url = URL + "/postProductByObject";

        // 设置请求的 Content-Type 为：application/json
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        // 设置 Accept 向服务器表明客户端可处理的内容类型
        header.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        // 直接将实体 Product 作为请求参数传入，底层利用 Jackson 框架序列化成 JSON 串发送请求
        HttpEntity<Product> request = new HttpEntity<>(new Product(2, "Macbook Pro", BigDecimal.valueOf(10000), new Date()), header);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("exchange: " + exchange);
    }

    @Test
    void putByproduct() {
        String url = URL + "/putByproduct";

        // PUT 方法请求
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        Product product = new Product(101, "iWatch", BigDecimal.valueOf(2333), new Date());
        String productStr = "id=" + product.getId() + "&name=" + product.getName() + "&price=" + product.getPrice() + "&date=" + product.getDate();
        HttpEntity<String> request = new HttpEntity<>(productStr, header);
        restTemplate.put(url, request);
    }

    @Test
    void putProductById() {
        String url = URL + "/putProductById/{id}";

        // PUT 方法请求
        Map<String, String> params = Maps.newHashMap();
        params.put("id", "108");
        Product product = new Product(108, "Nike", BigDecimal.valueOf(888.88), new Date());
        restTemplate.put(url, product, params);
    }

    @Test
    void deleteByRestful() {
        String url = URL + "/delete/{id}";

        // DELETE RESTful 方法请求
        restTemplate.delete(url, 101);
    }

    @Test
    void upload() throws IOException {
        // Controller 有两种接收方法
        // 1. @RequestParam("file") MultipartFile file
//        String url = URL + "/upload";

        // 2. MultipartRequest request
        String url = URL + "/upload2";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", getUserFileResource());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("upload: " + responseEntity);
        System.out.println("response body: " + responseEntity.getBody());

    }

    private static Resource getUserFileResource() throws IOException {
        Path path = Paths.get("/Users/vincent/IDEA_Project/my_project/calling-third-party-api/src/test/java/com/vincent/callingthirdpartyapi/resttemplate");

        // todo replace tempFile with a real file
        Path tempFile = Files.createTempFile(path, "upload", ".txt");
        Files.write(tempFile, "some test content...\nline1\nline2".getBytes());
        System.out.println("uploading: " + tempFile);
        File file = tempFile.toFile();
        //to upload in-memory bytes use ByteArrayResource instead
        return new FileSystemResource(file);
    }
}


















