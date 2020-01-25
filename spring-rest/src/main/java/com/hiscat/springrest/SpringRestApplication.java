package com.hiscat.springrest;

import com.hiscat.springrest.entity.Employee;
import com.hiscat.springrest.entity.Order;
import com.hiscat.springrest.entity.Status;
import com.hiscat.springrest.repository.EmployeeRepository;
import com.hiscat.springrest.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Administrator
 */
@SpringBootApplication
@Slf4j
public class SpringRestApplication {

    @Bean
    CommandLineRunner initDb(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        return args -> {
            LOGGER.info("Preloading " + employeeRepository.save(new Employee("Hello", "World", "programmer")));
            LOGGER.info("Preloading " + employeeRepository.save(new Employee("你好", "世界", "programmer")));
            LOGGER.info("Preloading " + orderRepository.save(new Order("MacBook Pro", Status.COMPLETED)));
            LOGGER.info("Preloading " + orderRepository.save(new Order("iPhone", Status.IN_PROGRESS)));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringRestApplication.class, args);
    }

}
