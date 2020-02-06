package com.hiscat.resource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author Administrator
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
        .and().authorizeRequests().anyRequest().permitAll()
//                .and().cors()
//                .and().authorizeRequests().anyRequest().authenticated()

        ;
    }

}
