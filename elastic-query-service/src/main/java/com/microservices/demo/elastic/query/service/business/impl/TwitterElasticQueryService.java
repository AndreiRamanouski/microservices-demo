package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.QueryType;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceWordCountResponseModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchCorruptionException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TwitterElasticQueryService implements ElasticQueryService {

    //    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;
    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;
    private final WebClient.Builder webclientBuilder;

    public TwitterElasticQueryService(
            ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler,
            ElasticQueryClient<TwitterIndexModel> elasticQueryClient,
            ElasticQueryServiceConfigData elasticQueryServiceConfigData, Builder webclientBuilder) {
        this.elasticQueryServiceResponseModelAssembler = elasticQueryServiceResponseModelAssembler;
        //        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
        this.elasticQueryClient = elasticQueryClient;
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
        this.webclientBuilder = webclientBuilder;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        log.info("getDocumentById");
        //        return elasticToResponseModelTransformer.getResponseModel(elasticQueryClient.getIndexById(id));
        return elasticQueryServiceResponseModelAssembler.toModel(elasticQueryClient.getIndexById(id));
    }

    @Override
    public ElasticQueryServiceAnalyticsResponseModel getDocumentByText(String text, String accessToken) {
        log.info("getDocumentByText");
        //        return elasticToResponseModelTransformer
        //        .getResponseModels(elasticQueryClient.getIndexModelById(text));
        List<ElasticQueryServiceResponseModel> elasticQueryServiceResponseModels = elasticQueryServiceResponseModelAssembler.toModels(
                elasticQueryClient.getIndexModelById(text));

        return ElasticQueryServiceAnalyticsResponseModel.builder()
                .queryServiceResponseModels(elasticQueryServiceResponseModels)
                .wordCount(getWordCount(text, accessToken).getWordCount())
                .build();
    }

    private ElasticQueryServiceWordCountResponseModel getWordCount(String text, String accessToken) {
        log.info("getWordCount");
        if (QueryType.KAFKA_STATE_STORE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            ElasticQueryServiceConfigData.Query query = elasticQueryServiceConfigData.getQueryFromKafkaStateStore();
            return webclientBuilder.build()
                    .method(HttpMethod.GET)
                    .uri(query.getUri(), uriBuilder -> uriBuilder.build(text))
                    .headers(h -> h.setBearerAuth(accessToken))
                    .accept(MediaType.valueOf(query.getAccept()))
                    .retrieve()
                    .onStatus(s -> s.equals(HttpStatus.UNAUTHORIZED),
                            clientResponse -> Mono.just(new BadCredentialsException("Not Authenticated")))
                    .onStatus(HttpStatus::is4xxClientError,
                            clientResponse -> Mono.just(new ElasticsearchCorruptionException(
                                    clientResponse.statusCode().getReasonPhrase())))
                    .onStatus(HttpStatus::is5xxServerError,
                            clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())))
                    .bodyToMono(ElasticQueryServiceWordCountResponseModel.class)
                    .log()
                    .block();
        } else {
            return ElasticQueryServiceWordCountResponseModel.builder()
                    .word(text)
                    .wordCount(0L)
                    .build();
        }

    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocument() {
        log.info("getAllDocument");
        //        return elasticToResponseModelTransformer.getResponseModels(elasticQueryClient.getAllIndexModels());
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }
}
