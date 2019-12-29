package com.hiscat.springnacos.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 */
@RestController
public class EchoController {
    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string) {
        return "Hello Nacos Discovery " + string;
    }
}