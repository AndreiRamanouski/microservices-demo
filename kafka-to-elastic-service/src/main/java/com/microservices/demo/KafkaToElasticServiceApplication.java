package com.microservices.demo;

import com.microservices.demo.config.KafkaConsumerConfigData;
import com.microservices.demo.consumer.impl.TwitterKafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableConfigurationProperties({KafkaConsumerConfigData.class})
@Slf4j
@ComponentScan(basePackages = "com.microservices.demo")
public class KafkaToElasticServiceApplication implements CommandLineRunner {

    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    private final TwitterKafkaConsumer twitterKafkaConsumer;

    public KafkaToElasticServiceApplication(KafkaConsumerConfigData kafkaConsumerConfigData,
            TwitterKafkaConsumer twitterKafkaConsumer) {
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.twitterKafkaConsumer = twitterKafkaConsumer;
    }

    public static void main(String[] args) {

        SpringApplication.run(KafkaToElasticServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting {}",  kafkaConsumerConfigData.getConsumerGroupId());
        log.info("Starting {}",  kafkaConsumerConfigData.getMaxPollRecords());

        log.info("TwitterKafka {}", twitterKafkaConsumer.toString());
    }
}

