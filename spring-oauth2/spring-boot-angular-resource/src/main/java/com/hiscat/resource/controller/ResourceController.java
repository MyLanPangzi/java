package com.hiscat.resource.controller;

import com.hiscat.resource.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Administrator
 */
@RestController
@Slf4j
public class ResourceController {
    @GetMapping("/")
    public Message home() {
        LOGGER.info("/home in");
        return new Message(UUID.randomUUID().toString());
    }
}

