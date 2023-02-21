package com.microservices.demo.consumer.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaConsumerConfigData;
import com.microservices.demo.config.client.KafkaAdminClient;
import com.microservices.demo.consumer.KafkaConsumer;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.transformer.AvroToElasticModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaAdminClient kafkaAdminClient;

    private final KafkaConfigData kafkaConfigData;

    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    private final AvroToElasticModelTransformer avroToElasticModelTransformer;
    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;

    public TwitterKafkaConsumer(KafkaListenerEndpointRegistry listenerEndpointRegistry,
            KafkaAdminClient adminClient,
            KafkaConfigData configData, KafkaConsumerConfigData kafkaConsumerConfigData,
            AvroToElasticModelTransformer avroToElasticModelTransformer,
            ElasticIndexClient<TwitterIndexModel> elasticIndexClient) {
        this.kafkaListenerEndpointRegistry = listenerEndpointRegistry;
        this.kafkaAdminClient = adminClient;
        this.kafkaConfigData = configData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.avroToElasticModelTransformer = avroToElasticModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId())
                .start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}", autoStartup = "false")
    public void receive(@Payload List<TwitterAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of message received with keys {}, partitions {} and offsets {}, " +
                        "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());
        log.info("messages {}", messages);
        List<TwitterIndexModel> elasticModels = avroToElasticModelTransformer.getElasticModels(messages);
        List<String> save = elasticIndexClient.save(elasticModels);
        log.info("Saved to elasticsearch with ids {}", save.toArray());

    }
}
