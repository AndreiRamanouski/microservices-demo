package com.microservices.demo.elastic.query.service.impl;

import com.microservices.demo.common.util.CollectionUtil;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.repository.TwitterElasticsearchQueryRepository;
import com.microservices.demo.elastic.query.service.ElasticQueryClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@Slf4j
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {


    private final TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository;

    public TwitterElasticRepositoryQueryClient(
            TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository) {
        this.twitterElasticsearchQueryRepository = twitterElasticsearchQueryRepository;
    }

    @Override
    public TwitterIndexModel getIndexById(String id) {
        TwitterIndexModel twitterIndexModel = twitterElasticsearchQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No document found with id: " + id));
        log.info("Document with id {} retrieved successfully", id);
        return twitterIndexModel;

    }

    @Override
    public List<TwitterIndexModel> getIndexModelById(String text) {
        List<TwitterIndexModel> byText = twitterElasticsearchQueryRepository.findByText(text);
        log.info("Document with text {} retrieved successfully, number of documents is {}", text, byText.size());
        return byText;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {

        Iterable<TwitterIndexModel> all = twitterElasticsearchQueryRepository.findAll();
        List<TwitterIndexModel> listFromIterable = CollectionUtil.getInstance().getListFromIterable(all);
        log.info("Document retrieved successfully the number is {}", listFromIterable.size());
        return listFromIterable;
    }
}
