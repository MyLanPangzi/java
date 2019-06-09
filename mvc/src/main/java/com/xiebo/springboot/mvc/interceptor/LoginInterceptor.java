package com.xiebo.springboot.mvc.interceptor;

import com.xiebo.springboot.mvc.controller.IndexController;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute(IndexController.USER);
        if (user == null) {
            request.getRequestDispatcher("/index").forward(request, response);
            return false;
        }
        return true;
    }
}
