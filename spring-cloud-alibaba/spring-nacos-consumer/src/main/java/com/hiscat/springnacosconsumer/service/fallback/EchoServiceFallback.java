package com.hiscat.springnacosconsumer.service.fallback;

import com.hiscat.springnacosconsumer.service.EchoService;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class EchoServiceFallback implements EchoService {

    @Override
    public String hello(String msg) {
        return "net busy...";
    }
}
