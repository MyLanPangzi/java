package com.hiscat.springnacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringNacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNacosProviderApplication.class, args);
    }

}
