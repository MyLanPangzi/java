package com.hiscat.springnacosconsumer.controller;

import com.hiscat.springnacosconsumer.service.EchoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 */
@RestController
@AllArgsConstructor
public class ConsumerController {

    private final RestTemplate restTemplate;

    private final EchoService echoService;

    @GetMapping(value = "/echo/{str}")
    public String echo(@PathVariable String str) {
        return echoService.hello(str);
    }
}