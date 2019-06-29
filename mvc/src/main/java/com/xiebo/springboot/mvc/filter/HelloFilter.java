package com.xiebo.springboot.mvc.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class HelloFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("this is hello filter {}", System.currentTimeMillis());
        chain.doFilter(request, response);
    }
}
