package com.xiebo.springboot.initializrhello;

import com.xiebo.springboot.initializrhello.bean.Person;
import com.xiebo.springboot.initializrhello.service.HelloService;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class InitializrHelloApplicationTests {
    @Autowired
    Person person;

    @Autowired
    HelloService helloService;

    @Test
    public void testHelloService() {
        assertNotNull(helloService);
    }

    @Test
    public void contextLoads() {
        System.out.println(person);
    }

}
