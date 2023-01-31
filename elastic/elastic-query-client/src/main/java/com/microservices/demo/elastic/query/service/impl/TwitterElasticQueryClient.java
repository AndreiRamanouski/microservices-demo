package com.microservices.demo.elastic.query.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.config.ElasticQueryConfigData;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.util.ElasticQueryUtil;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;

    public TwitterElasticQueryClient(ElasticConfigData elasticConfigData, ElasticQueryConfigData elasticQueryConfigData,
            ElasticsearchOperations elasticsearchOperations, ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticQueryUtil = elasticQueryUtil;
    }

    @Override
    public TwitterIndexModel getIndexById(String id) {
        Query searchQueryById = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> searchResult = elasticsearchOperations.searchOne(searchQueryById,
                TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        if (Objects.isNull(searchResult)) {
            log.error("No document found with id: {}", id);
            throw new RuntimeException("No document found with id: " + id);
        }
        log.info("Document with id {} retrieved successfully", id);
        return searchResult.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelById(String text) {
        Query searchQueryByFieldText = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(),
                text);
        SearchHits<TwitterIndexModel> searchResult = elasticsearchOperations.search(searchQueryByFieldText,
                TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));

        log.info("Document with text {} retrieved successfully", text);
        return searchResult.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query searchQueryForAll = elasticQueryUtil.getSearchQueryForAll();
        SearchHits<TwitterIndexModel> search = elasticsearchOperations.search(searchQueryForAll,
                TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        log.info("Document retrieved successfully the number is {}", search.get().count());
        return search.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
