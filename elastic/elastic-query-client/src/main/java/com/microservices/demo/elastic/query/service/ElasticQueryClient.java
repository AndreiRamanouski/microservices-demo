package com.microservices.demo.elastic.query.service;

import com.microservices.demo.elastic.model.IndexModel;
import java.util.List;

public interface ElasticQueryClient<T extends IndexModel> {

    T getIndexById(String id);

    List<T> getIndexModelById(String text);

    List<T> getAllIndexModels();


}
