package com.hiscat.springhello.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@ConfigurationProperties(prefix = "hello")
@Data
public class HelloProperties {
    private String greeting;
}
