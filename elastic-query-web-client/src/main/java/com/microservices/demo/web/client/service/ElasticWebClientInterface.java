package com.microservices.demo.web.client.service;

import com.microservices.demo.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.web.client.model.ElasticQueryWebClientResponseModel;
import java.util.List;

public interface ElasticWebClientInterface {

    List<ElasticQueryWebClientResponseModel> getDataByText(
            ElasticQueryWebClientRequestModel elasticQueryWebClientRequestModel);
}
