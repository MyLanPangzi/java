package com.hiscat.spring.annotation.config.profile;

import com.hiscat.spring.annotation.entity.Car;
import com.hiscat.spring.annotation.entity.Cat;
import com.hiscat.spring.annotation.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Administrator
 */
@Configuration
public class ProfileConfig {
    @Bean
    @Profile("dev")
    Car car() {
        return new Car();
    }

    @Bean
    @Profile("test")
    Cat cat() {
        return new Cat();
    }

    @Bean
    @Profile("prod")
    Person person() {
        return new Person();
    }
}
