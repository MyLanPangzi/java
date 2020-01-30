package com.hiscat.springdubboprovider.service.impl;

import com.hiscat.springbubbo.api.EchoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Administrator
 */
@Service(version = "1.0.0")
public class EchoServiceImpl implements EchoService {

    @Value("${server.port}")
    private String port;

    @Override
    public String echo(String msg) {
        return "hello " + msg + " " + port;
    }
}
