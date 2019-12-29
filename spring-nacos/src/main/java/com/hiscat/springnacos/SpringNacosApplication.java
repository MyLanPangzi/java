package com.hiscat.springnacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNacosApplication.class, args);
    }

}
