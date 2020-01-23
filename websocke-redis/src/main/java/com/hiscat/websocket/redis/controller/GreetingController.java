package com.hiscat.websocket.redis.controller;

import com.hiscat.websocket.redis.message.HelloMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * @author Administrator
 */
@Controller
@AllArgsConstructor
@Slf4j
public class GreetingController {
    private final StringRedisTemplate stringRedisTemplate;

    @MessageMapping("/hello/{var}")
    public void greeting(@DestinationVariable String var, @Payload HelloMessage message) {
        LOGGER.info("var:{}, message:{}", var, message);
        stringRedisTemplate.convertAndSend(String.format("/topic/%s", var), message.getName());
    }

}