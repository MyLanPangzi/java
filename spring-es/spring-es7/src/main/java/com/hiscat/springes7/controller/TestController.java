package com.hiscat.springes7.controller;

import com.hiscat.springes7.bean.Person;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author hiscat
 */
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class TestController {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchRestTemplate restTemplate;

    @PostMapping("/person")
    public String save(@RequestBody Person person) {
        return elasticsearchOperations.save(person).getId();
    }

    @GetMapping("/person/{id}")
    public Person findById(@PathVariable("id") Long id) {

        return restTemplate.get(id.toString(), Person.class);
    }
}