package com.microservices.demo.web.client.service;

import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import java.util.List;

public interface ElasticWebClientInterface {

    ElasticQueryServiceAnalyticsResponseModel getDataByText(
            ElasticQueryWebClientRequestModel elasticQueryWebClientRequestModel);
}
