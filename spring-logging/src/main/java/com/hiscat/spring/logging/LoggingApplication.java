package com.hiscat.spring.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 */
@SpringBootApplication
@Slf4j
public class LoggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggingApplication.class, args);
        LOGGER.debug("debug");
        LOGGER.trace("trace");
        LOGGER.info("info");
        LOGGER.warn("warn");
        LOGGER.error("error");
    }

}
