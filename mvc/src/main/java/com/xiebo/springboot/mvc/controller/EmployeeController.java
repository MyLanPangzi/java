package com.xiebo.springboot.mvc.controller;

import com.xiebo.springboot.mvc.dao.DepartmentDao;
import com.xiebo.springboot.mvc.dao.EmployeeDao;
import com.xiebo.springboot.mvc.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    @GetMapping("/employees")
    public ModelAndView employees() {
        return new ModelAndView("employee/list")
                .addObject("employees", this.employeeDao.getAll());
    }

    @GetMapping("/employee")
    public ModelAndView toAdd() {
        return new ModelAndView("employee/add")
                .addObject("departments", this.departmentDao.getDepartments());
    }

    @PostMapping("/employee")
    public ModelAndView add(Employee employee) {
        this.employeeDao.save(employee);
        return new ModelAndView("redirect:/employees");
    }
}
