package com.hiscat.spring.annotation.config.value;

import com.hiscat.spring.annotation.entity.Cat;
import com.hiscat.spring.annotation.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author hiscat
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@PropertySource("classpath:person.properties")
public class PropertyValueConfig {
    @Bean
    Cat cat() {
        return new Cat();
    }

    @Bean
    Person person() {
        return new Person();
    }
}
