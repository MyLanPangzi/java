package com.hiscat.springnacosconsumer.service;

import com.hiscat.springnacosconsumer.service.fallback.EchoServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Administrator
 */

@FeignClient(value = "nacos-service-provider", fallback = EchoServiceFallback.class)
public interface EchoService {
    /**
     * greeting
     *
     * @param msg msg
     * @return greeting
     */
    @GetMapping("/echo/{msg}")
    String hello(@PathVariable String msg);
}
