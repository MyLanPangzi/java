package com.xiebo.springboot.mvc.config;

import com.xiebo.springboot.mvc.interceptor.LoginInterceptor;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

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
            registry.addViewController("/main").setViewName("dashboard");
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry
                    .addInterceptor(new LoginInterceptor())
                    .excludePathPatterns("/", "/index", "/login", "/hello", "/error")
                    .addPathPatterns("/*");
        }

    }

    @Component
    private static class CustomDefaultErrorAttributes extends DefaultErrorAttributes {
        @Override
        public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
            Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
            errorAttributes.put("Hello", "world");
            return errorAttributes;
        }
    }

    @Component("localeResolver")
    public static class MyLocaleResolver implements LocaleResolver {
        private AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String localParameter = request.getParameter("l");
            if (StringUtils.isEmpty(localParameter)) {
                return this.acceptHeaderLocaleResolver.resolveLocale(request);
            }
            String[] split = localParameter.split("_");
            if (split.length != 2) {
                return Locale.getDefault();
            }
            return new Locale(split[0], split[1]);
        }

        @Override
        public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
            this.acceptHeaderLocaleResolver.setLocale(request, response, locale);
        }
    }

//    @Bean
//    public HttpMessageConverters httpMessageConverters() {
//        HttpMessageConverter<?> httpMessageConverter = new FastJsonHttpMessageConverter();
//
//        return new HttpMessageConverters(httpMessageConverter);
//    }

    @Component
    private static class MyViewResolver implements ViewResolver {

        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            return null;
        }
    }
}
