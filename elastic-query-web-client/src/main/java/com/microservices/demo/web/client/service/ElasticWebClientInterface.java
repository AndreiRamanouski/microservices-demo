package com.microservices.demo.web.client.service;

import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import java.util.List;

public interface ElasticWebClientInterface {

    List<ElasticQueryWebClientResponseModel> getDataByText(
            ElasticQueryWebClientRequestModel elasticQueryWebClientRequestModel);
}
