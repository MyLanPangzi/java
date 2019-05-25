package com.xiebo.springboot.initializrhello;

import com.xiebo.springboot.initializrhello.config.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitializrHelloApplicationTests {
    @Autowired
    Person person;

    @Test
    public void contextLoads() {
        System.out.println(person);
    }

}
