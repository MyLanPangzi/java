package com.hiscat.springdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableEurekaServer
public class SpringDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDiscoveryApplication.class, args);
    }

}
