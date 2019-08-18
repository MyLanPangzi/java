package com.hiscat.spring.annotation.config.lifecycle;

import com.hiscat.spring.annotation.entity.Car;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author hiscat
 */
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Car) {
            System.out.println("postProcessBeforeInitialization");
            Car car = (Car) bean;
            ((Car) bean).setName("bmw");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Car) {
            System.out.println("postProcessAfterInitialization");
        }
        return bean;
    }
}
