package com.hiscat.spring.annotation.config.imp;

import com.hiscat.spring.annotation.entity.Person;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hiscat
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class FactoryBeanConfig {

    @Bean
    FactoryBean<Person> person() {
        return new FactoryBean<>() {
            @Override
            public boolean isSingleton() {
                return true;
            }

            @Override
            public Person getObject() {
                return new Person();
            }

            @Override
            public Class<?> getObjectType() {
                return Person.class;
            }
        };
    }
}
