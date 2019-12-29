package com.hiscat.dubbo.hello.provider;

import com.hiscat.doubbo.hello.GreetingsService;

/**
 * @author Administrator
 */
public class GreetingsServiceImpl implements GreetingsService {

    public String sayHi(String name) {
        return "hi " + name;
    }
}
