package com.vincent.callingthirdpartyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CallingThirdPartyApiApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(CallingThirdPartyApiApplicationTests.class, args);
    }

}
