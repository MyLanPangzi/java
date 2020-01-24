package com.hiscat.springrest.repository;

import com.hiscat.springrest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Administrator
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
