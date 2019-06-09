package com.xiebo.springboot.mvc.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@Configuration
//@EnableWebMvc
public class AppConfig {

    @Component
    public static class MyWebMvcConfigure implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/echo").setViewName("hello");
            registry.addViewController("/").setViewName("index");
            registry.addViewController("/index").setViewName("index");
        }

    }

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        HttpMessageConverter<?> httpMessageConverter = new FastJsonHttpMessageConverter();
        return new HttpMessageConverters(httpMessageConverter);
    }

    @Component
    private static class MyViewResolver implements ViewResolver {

        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            return null;
        }
    }
}
