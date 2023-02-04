package com.microservices.demo.reactive.elastic.query.web.client;

import com.microservices.demo.config.ElasticQueryWebClientConfigData.WebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
@EnableConfigurationProperties({WebClient.class})
public class ReactiveElasticQueryWebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveElasticQueryWebClientApplication.class, args);
    }

}
