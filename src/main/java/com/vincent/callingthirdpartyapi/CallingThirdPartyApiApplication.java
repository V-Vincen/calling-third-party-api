package com.vincent.callingthirdpartyapi;

import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.WarnStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author vincent
 */
@SpringBootApplication
public class CallingThirdPartyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallingThirdPartyApiApplication.class, args);
    }

}
