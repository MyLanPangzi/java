package com.histcat.springboot.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.histcat.springboot.jdbc.entity.Department;
import com.histcat.springboot.jdbc.entity.Employee;
import com.histcat.springboot.jdbc.mapper.DepartmentMapper;
import com.histcat.springboot.jdbc.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Test
    public void testDataSource() {
        assertNotNull(dataSource);
    }

    @Test
    public void testInitializeSize() {
        DruidDataSource druidDataSource = (DruidDataSource) this.dataSource;
        assertEquals(5, druidDataSource.getInitialSize());
    }

    @Test
    public void testDepartmentMapperInsert() {
        Department department = new Department(null, "财务部门");
        Integer id = this.departmentMapper.insert(department);
        assertNotNull(department.getId());
        assertEquals(id, department.getId());
    }

    @Test
    public void testEmployeeMapperInsert() {
        Employee employee = Employee
                .builder()
                .lastName("测试")
                .email("test@emai.com")
                .gender(0)
                .dId(1)
                .build();
        Integer id = this.employeeMapper.insert(employee);
        assertEquals(employee.getId(), id);
    }
}
