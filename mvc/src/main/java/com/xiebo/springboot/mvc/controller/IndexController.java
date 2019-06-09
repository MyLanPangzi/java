package com.xiebo.springboot.mvc.controller;

import com.xiebo.springboot.mvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping("/hello")
    public ModelAndView hello() {

        return new ModelAndView("/hello")
                .addObject("hello", "world");
    }

    @GetMapping("/user")
    @ResponseBody
    public User user() {
        return new User("hello", "M");
    }
}
