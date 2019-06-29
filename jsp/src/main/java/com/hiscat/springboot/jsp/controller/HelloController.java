package com.hiscat.springboot.jsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String  hello() {
        return "success";
        //        return new ModelAndView("success")
//                .addObject("hello", "world");
    }
}
