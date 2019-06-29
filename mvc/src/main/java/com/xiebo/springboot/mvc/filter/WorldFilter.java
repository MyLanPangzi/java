package com.xiebo.springboot.mvc.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/servlet/world")
@Slf4j
public class WorldFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("this is world filter {}", System.currentTimeMillis());
        chain.doFilter(request, response);
    }
}
