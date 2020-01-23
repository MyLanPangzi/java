package com.hiscat.hello.hellomongo.repository;

import com.hiscat.hello.hellomongo.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Administrator
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {
    /**
     * 根据姓查询用户
     *
     * @param firstName 姓
     * @return 用户
     */
    Customer findByFirstName(String firstName);

    /**
     * 根据名查询用户列表
     *
     * @param lastName 名
     * @return 用户列表
     */
    List<Customer> findByLastName(String lastName);
}
