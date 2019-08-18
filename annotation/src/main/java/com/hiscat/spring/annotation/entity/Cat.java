package com.hiscat.spring.annotation.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class Cat {
    @Value("小花")
    private String name;
    @Value("#{2}")
    private Integer age;
}
