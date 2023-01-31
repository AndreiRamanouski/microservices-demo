package com.microservices.demo.elastic.query.service;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import com.microservices.demo.config.UserConfigData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({ElasticQueryServiceConfigData.class, UserConfigData.class})
@ComponentScan(basePackages = "com.microservices.demo")
public class ElasticQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryServiceApplication.class, args);
    }
}
