package com.microservices.demo.elastic.query.service.model;

import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryServiceResponseModelV2 extends RepresentationModel<ElasticQueryServiceResponseModel> {

    private Long id;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}
