package com.xiebo.springboot.mvc.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

@Slf4j
public class MyServletListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        LOGGER.info("this is servlet request listener path: ", sre.getServletRequest().getRemoteHost());
    }
}
