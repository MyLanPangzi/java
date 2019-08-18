package com.hiscat.spring.annotation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author hiscat
 */
@Data
@AllArgsConstructor
@Builder
public class Car implements InitializingBean, DisposableBean {
    @Value("bmw")
    private String name;
    @Value("#{4}")
    private Integer wheels;
    public Car() {
        System.out.println("construct");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("postConstruct");
    }
    @Override
    public void afterPropertiesSet() {
        System.out.println("afterPropertiesSet");
    }
    public void init() {
        System.out.println("init");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("preDestroy");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }

    public void close() {
        System.out.println("close");
    }
}
