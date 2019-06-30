package com.histcat.springboot.jdbc.mapper;

import com.histcat.springboot.jdbc.entity.Department;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

//@Mapper
public interface DepartmentMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into department (id, department_name)\n" +
            "values (NULL,#{departmentName})")
    Integer insert(Department department);



}
