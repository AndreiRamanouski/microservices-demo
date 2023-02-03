package com.microservices.demo.web.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
//@EnableConfigurationProperties({ElasticQueryWebClientConfigData.class})
public class ElasticQueryWebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryWebClientApplication.class, args);
    }
}
