package com.xiebo.springboot.initializrhello.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ConfigurationProperties注解表示，读取yml/properties文件中的那个属性，用作填充
 * <p>
 * \@Value无法使用复杂类型，无法进行验证，字段名不够灵活，需写多次，可以使用SpEl
 * \@ConfigurationProperties声明一次即可绑定，可进行JSR303验证，字段名灵活绑定
 */
@Component
@Validated
@PropertySource("classpath:person.properties")
@ConfigurationProperties(prefix = "person")
public class Person {
    //    @Value("${person.name}")
    private String name;
    //    @Value("${person.last-name}")
    private String lastName;
    //    @Value("#{19}")
//    @Max(20)
    private Integer age;
    //    @Value("#{new java.util.Date()}")
    private Date birthday;
    //    @Value("false")
    private Boolean isTeenager;
    //    @Value("person.teenager")
    private List<Object> hobbies;
    private Map<String, Object> maps;
    private Dog dog;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", isTeenager=" + isTeenager +
                ", hobbies=" + hobbies +
                ", maps=" + maps +
                ", dog=" + dog +
                '}';
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getTeenager() {
        return isTeenager;
    }

    public void setTeenager(Boolean teenager) {
        isTeenager = teenager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Object> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Object> hobbies) {
        this.hobbies = hobbies;
    }

    public Map<String, Object> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, Object> maps) {
        this.maps = maps;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}
