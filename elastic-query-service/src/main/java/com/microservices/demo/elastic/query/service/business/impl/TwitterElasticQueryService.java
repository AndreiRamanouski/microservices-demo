package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwitterElasticQueryService implements ElasticQueryService {

    //    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;
    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

    public TwitterElasticQueryService(
            ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler,
            ElasticQueryClient<TwitterIndexModel> elasticQueryClient) {
        this.elasticQueryServiceResponseModelAssembler = elasticQueryServiceResponseModelAssembler;
        //        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
        this.elasticQueryClient = elasticQueryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        log.info("getDocumentById");
        //        return elasticToResponseModelTransformer.getResponseModel(elasticQueryClient.getIndexById(id));
        return elasticQueryServiceResponseModelAssembler.toModel(elasticQueryClient.getIndexById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        log.info("getDocumentByText");
        //        return elasticToResponseModelTransformer
        //        .getResponseModels(elasticQueryClient.getIndexModelById(text));
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getIndexModelById(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocument() {
        log.info("getAllDocument");
        //        return elasticToResponseModelTransformer.getResponseModels(elasticQueryClient.getAllIndexModels());
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }
}
