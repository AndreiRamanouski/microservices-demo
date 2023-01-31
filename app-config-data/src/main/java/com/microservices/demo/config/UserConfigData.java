package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Data
@Primary
@Configuration
@ConfigurationProperties(prefix = "user-config")
public class UserConfigData {
    private String username;
    private String password;
    private String[] roles;
}
