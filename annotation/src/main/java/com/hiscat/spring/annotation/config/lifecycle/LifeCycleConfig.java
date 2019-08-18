package com.hiscat.spring.annotation.config.lifecycle;

import com.hiscat.spring.annotation.entity.Car;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hiscat
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class LifeCycleConfig {
    @Bean
    BeanPostProcessor beanPostProcessor() {
        return new MyBeanPostProcessor();
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    Car car() {
        return new Car();
    }
}
