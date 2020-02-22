package com.hiscat.codec.springrest.repository;

import com.hiscat.codec.springrest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Administrator
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
