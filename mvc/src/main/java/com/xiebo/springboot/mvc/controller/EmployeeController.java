package com.xiebo.springboot.mvc.controller;

import com.xiebo.springboot.mvc.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;

    @GetMapping("/employees")
    public ModelAndView employees() {
        return new ModelAndView("employee/list")
                .addObject("employees", this.employeeDao.getAll());
    }
}
