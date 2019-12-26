package com.hiscat.springdiscoveryclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringDiscoveryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDiscoveryClientApplication.class, args);
    }

}
