package com.xiebo.springboot.mvc.controller;

import com.xiebo.springboot.mvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    public static final String USER = "user";

    @GetMapping("/hello")
    public ModelAndView hello() {

        return new ModelAndView("/hello")
                .addObject("hello", "world");
    }

    @PostMapping("/login")
    public ModelAndView login(User user, HttpSession session) {
        if (StringUtils.isEmpty(user.getUsername()) ||  !"123456".equals(user.getPassword())) {
            return new ModelAndView("index")
                    .addObject("msg", "用户名或密码错误！");
        }
        session.setAttribute(USER, user);
        return new ModelAndView("redirect:/main");
    }

    @GetMapping("/user")
    @ResponseBody
    public User user() {
        return new User("hello", "M");
    }
}
