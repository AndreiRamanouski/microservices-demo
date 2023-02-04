package com.microservices.demo.reactive.elastic.query.service.api;

import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.reactive.elastic.query.service.business.ElasticQueryService;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/documents")
@Slf4j
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @PostMapping(value = "/get-doc-by-text",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ElasticQueryServiceResponseModel> findAllByText(
            @RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        log.info("findAllByText");
        Flux<ElasticQueryServiceResponseModel> response = elasticQueryService.findDocumentsByText(
                requestModel.getText());
        response = response.log();
        log.info("Returning from query service for text {}", requestModel.getText());
        return response;
    }
}
