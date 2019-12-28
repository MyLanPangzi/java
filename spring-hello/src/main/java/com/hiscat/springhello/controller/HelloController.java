package com.hiscat.springhello.controller;

import com.hiscat.springhello.config.HelloProperties;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@AllArgsConstructor
public class HelloController {
    private final HelloProperties helloProperties;

    @GetMapping("/hello")
    public String hello() {
        return "Hello " + helloProperties.getGreeting();
    }
}
