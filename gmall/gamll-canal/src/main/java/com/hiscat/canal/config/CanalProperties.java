package com.hiscat.canal.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hiscat
 */
@Data
@Component
@ConfigurationProperties(prefix = "canal")
public class CanalProperties {
    private String username;
    private String password;
    private Long maxIdleTime;
    private String destination;
    private String host;
    private Integer pot;
    private String database;
    private Integer batchSize;
}
