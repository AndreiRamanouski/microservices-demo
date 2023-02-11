package com.microservices.demo.web.client.api;

import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.web.client.service.ElasticWebClientInterface;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QueryController {

    private final ElasticWebClientInterface elasticWebClientService;

    private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

    public QueryController(ElasticWebClientInterface elasticWebClientService) {
        this.elasticWebClientService = elasticWebClientService;
    }

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }

    @PostMapping("/query-by-text")
    public String queryByText(@Valid ElasticQueryWebClientRequestModel requestModel,
            Model model) {
        LOG.info("Querying with text {}", requestModel.getText());
        ElasticQueryServiceAnalyticsResponseModel responseModel = elasticWebClientService.getDataByText(requestModel);
        //        responseModels.add(ElasticQueryWebClientResponseModel.builder()
        //                .id("1")
        //                .text(requestModel.getText())
        //                .build());
        model.addAttribute("elasticQueryWebClientResponseModels", responseModel.getQueryServiceResponseModels());
        model.addAttribute("wordCount", responseModel.getWordCount());
        model.addAttribute("searchText", requestModel.getText());
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }

}
