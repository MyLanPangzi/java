package com.hiscat.es6.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author hiscat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "marvel", type = "characters")
public class Person {
    @Id
    private String id;
    private String name;
    private int age;
}