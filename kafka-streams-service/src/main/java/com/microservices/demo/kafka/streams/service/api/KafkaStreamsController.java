package com.microservices.demo.kafka.streams.service.api;

import com.microservices.demo.kafka.streams.service.model.KafkaStreamsResponseModel;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import javax.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/")
@RestController
@Slf4j
public class KafkaStreamsController {


    private final StreamsRunner<String, Long> kafkaStreamsRunner;


    public KafkaStreamsController(StreamsRunner<String, Long> kafkaStreamsRunner) {
        this.kafkaStreamsRunner = kafkaStreamsRunner;
    }

    @GetMapping(value = "get-word-count-by-word/{word}")
    public @ResponseBody ResponseEntity<KafkaStreamsResponseModel> getWordCountByWord(
            @PathVariable @NotEmpty String word) {
        log.info("getWordCountByWord");
        Long valueByKey = kafkaStreamsRunner.getValueByKey(word);
        log.info("Word {} appeared {} times", word, valueByKey);
        return ResponseEntity.ok(KafkaStreamsResponseModel.builder()
                .word(word)
                .wordCount(valueByKey)
                .build());
    }
}
