package com.xiebo.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class App {

    @GetMapping("/hello")
    public String home() {
        return "hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
