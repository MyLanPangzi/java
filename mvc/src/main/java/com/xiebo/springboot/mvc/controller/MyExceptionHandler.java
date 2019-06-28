package com.xiebo.springboot.mvc.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(UserNotFindException.class)
    public Map<String, Object> userNotFind(Exception e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView error(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", 500);

        return new ModelAndView("forward:/error")
                .addObject("msg", "world");
    }
}
