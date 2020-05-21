package com.hiscat.gmallweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hiscat
 */
@SpringBootApplication
@MapperScan("com.hiscat.gmallweb.mapper")
public class GmallWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallWebApplication.class, args);
    }

}
