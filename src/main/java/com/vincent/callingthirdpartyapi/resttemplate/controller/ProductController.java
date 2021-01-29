package com.vincent.callingthirdpartyapi.resttemplate.controller;

import com.vincent.callingthirdpartyapi.resttemplate.dto.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * @author vincent
 */
@RequestMapping(value = "/product")
@RestController
public class ProductController {

    @GetMapping("/getProductByNoParams")
    public Product getProductByNoParams() {
        return new Product(1, "ProductA", BigDecimal.valueOf(6666.0), new Date());
    }

    @GetMapping("/getProductById")
    public Product getProductById(Integer id) {
        return new Product(id, "ProductC", BigDecimal.valueOf(6666.0), new Date());
    }

    @PostMapping("/postProductByForm")
    public String postProductByForm(Product product) {
        return product.toString();
    }

    @PostMapping("/postProductByObject")
    public String postProductByObject(@RequestBody Product product) {
        return product.toString();
    }

    @PutMapping("/putByproduct")
    public String putByproduct(Product product) {
        System.out.println(product);
        return String.format("%s 的产品更新成功", product.toString());
    }

    @PutMapping(value = "/putProductById/{id}")
    public ResponseEntity<Product> putProductById(@PathVariable("id") int id, @RequestBody Product product) {
        System.out.println("id:" + id);
        System.out.println(product);
        //TODO: Save employee details
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteByRestful(@PathVariable int id) {
        System.out.println(String.format("编号为 %s 的产品删除成功", id));
        return String.format("编号为 %s 的产品删除成功", id);
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        System.out.println(String.format("File name: %s", name));

        //todo save to a file via multipartFile.getInputStream()
        byte[] bytes = file.getBytes();
        System.out.println(String.format("File uploaded content:\n%s", new String(bytes)));
        return "file uploaded";
    }

    @PostMapping("/upload2")
    public String upload2(MultipartRequest request) {
        // Spring MVC 使用 MultipartRequest 接受带文件的 HTTP 请求
        MultipartFile file = request.getFile("file");
        String originalFilename = Optional.ofNullable(file).map(MultipartFile::getOriginalFilename).orElse(null);
        return String.format("upload success filename: %s", originalFilename);
    }
}
