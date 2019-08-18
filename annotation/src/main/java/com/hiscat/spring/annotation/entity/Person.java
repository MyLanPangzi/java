package com.hiscat.spring.annotation.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author hiscat
 */
@Data
public class Person {
    @Value(("${person.name}"))
    private String name;

    @Value(("${person.gender}"))
    private String gender;
}
