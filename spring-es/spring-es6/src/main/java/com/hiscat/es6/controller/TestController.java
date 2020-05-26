package com.hiscat.es6.controller;

import com.hiscat.es6.bean.Person;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.*;

/**
 * @author hiscat
 */
@RestController
@RequestMapping("/")
public class TestController {

  private final ElasticsearchOperations elasticsearchOperations;

  public TestController(ElasticsearchOperations elasticsearchOperations) { 
    this.elasticsearchOperations = elasticsearchOperations;
  }

  @PostMapping("/person")
  public String save(@RequestBody Person person) {

    IndexQuery indexQuery = new IndexQueryBuilder()
      .withId(person.getId())
      .withObject(person)
      .build();
    return elasticsearchOperations.index(indexQuery);
  }

  @GetMapping("/person/{id}")
  public Person findById(@PathVariable("id")  Long id) {
    return elasticsearchOperations
      .queryForObject(GetQuery.getById(id.toString()), Person.class);
  }
}