package com.hiscat.springes7.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author hiscat
 */
@Data
@Document(indexName = "person1" )
public class Person {
    @Id
    private String id;
    private String name;
    private int age;
}