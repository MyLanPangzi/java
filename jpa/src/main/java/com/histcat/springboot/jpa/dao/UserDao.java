package com.histcat.springboot.jpa.dao;

import com.histcat.springboot.jpa.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {

}
