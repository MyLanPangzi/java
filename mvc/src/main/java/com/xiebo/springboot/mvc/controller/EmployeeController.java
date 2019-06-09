package com.xiebo.springboot.mvc.controller;

import com.xiebo.springboot.mvc.dao.DepartmentDao;
import com.xiebo.springboot.mvc.dao.EmployeeDao;
import com.xiebo.springboot.mvc.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
                .addObject("employee", new Employee())
                .addObject("departments", this.departmentDao.getDepartments());
    }

    @PostMapping("/employee")
    public ModelAndView add(Employee employee) {
        this.employeeDao.save(employee);
        return new ModelAndView("redirect:/employees");
    }

    @GetMapping("/employee/{id}")
    public ModelAndView toEdit(@PathVariable("id") Integer id) {
        return new ModelAndView("employee/add")
                .addObject("employee", this.employeeDao.get(id))
                .addObject("departments", this.departmentDao.getDepartments());
    }

    @PutMapping("/employee/{id}")
    public ModelAndView edit(@PathVariable("id") Integer id, Employee employee) {
        this.employeeDao.save(employee);
        return new ModelAndView("redirect:/employees");
    }

    @DeleteMapping("/employee/{id}")
    public ModelAndView delete(@PathVariable("id") Integer id) {
        this.employeeDao.delete(id);
        return new ModelAndView("redirect:/employees");
    }
}
