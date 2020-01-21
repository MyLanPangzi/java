package com.histcat.springboot.redis;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Administrator
 */
@SpringBootApplication
@AllArgsConstructor
public class RedisApplication implements CommandLineRunner {

    StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Override
    public void run(String... args) {
        stringRedisTemplate.opsForValue().set("hello", "world");
    }
}
