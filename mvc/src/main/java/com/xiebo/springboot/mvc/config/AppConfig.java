package com.xiebo.springboot.mvc.config;

import com.xiebo.springboot.mvc.MvcApplication;
import com.xiebo.springboot.mvc.filter.HelloFilter;
import com.xiebo.springboot.mvc.listener.MyServletListener;
import com.xiebo.springboot.mvc.listener.MySessionListener;
import com.xiebo.springboot.mvc.servlet.HelloServlet;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
import java.time.Duration;
import java.util.Locale;
import java.util.Map;

@Configuration
//@EnableWebMvc
@ServletComponentScan(basePackageClasses = MvcApplication.class)
public class AppConfig {

    @Bean
    public ServletRegistrationBean<HelloServlet> servletRegistrationBean() {
        return new ServletRegistrationBean<>(new HelloServlet(), "/servlet/hello");
    }

    @Bean
    public FilterRegistrationBean<HelloFilter> filterRegistrationBean(ServletRegistrationBean servletRegistrationBean) {
        return new FilterRegistrationBean<>(new HelloFilter(), servletRegistrationBean);
    }

    @Bean
    public ServletListenerRegistrationBean<MyServletListener> servletListenerRegistrationBean() {
        return new ServletListenerRegistrationBean<>(new MyServletListener());
    }

    @Bean
    public ServletListenerRegistrationBean<MySessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(new MySessionListener());
    }


    @Component
    public static class CustomizationBean implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

        @Override
        public void customize(ConfigurableServletWebServerFactory server) {
            server.setPort(8080);
            Session session = new Session();
            session.setTimeout(Duration.ofSeconds(3600));
            server.setDisplayName("Hiscat");
            server.setSession(session);
        }

    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(8080);
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));
        return factory;
    }

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
//            registry
//                    .addInterceptor(new LoginInterceptor())
//                    .excludePathPatterns("/", "/index", "/login", "/hello", "/error")
//                    .addPathPatterns("/*");
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
        public View resolveViewName(String viewName, Locale locale)  {
            return null;
        }
    }
}
