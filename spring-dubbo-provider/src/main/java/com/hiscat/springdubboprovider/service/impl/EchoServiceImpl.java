package com.hiscat.springdubboprovider.service.impl;

import com.hiscat.springbubbo.api.EchoService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author Administrator
 */
@Service(version = "1.0.0")
public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String msg) {
        return "hello " + msg;
    }
}
