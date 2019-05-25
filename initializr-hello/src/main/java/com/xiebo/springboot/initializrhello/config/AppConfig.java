package com.xiebo.springboot.initializrhello.config;

import com.xiebo.springboot.initializrhello.service.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
//@ImportResource("classpath:beans.xml")
public class AppConfig {

    //    @Bean
    public HelloService helloService() {
        return new HelloService();
    }
}
