package com.hiscat.hello.hellomongo.controller;

import com.hiscat.hello.hellomongo.entity.Customer;
import com.hiscat.hello.hellomongo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@AllArgsConstructor
public class HelloController {

    private final CustomerRepository customerRepository;

    @GetMapping("/customer")
    public Customer findByFirstName(String firstName) {
        return customerRepository.findByFirstName(firstName);
    }
}
