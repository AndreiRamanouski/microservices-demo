package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);
    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @Operation(summary = "Get all elastic documents.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful response.", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))}),
    @ApiResponse(responseCode = "400", description = "Not Found."),
    @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @GetMapping("/v1")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocument();
        LOG.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get document by id v1.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful response.", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))}),
            @ApiResponse(responseCode = "400", description = "Not Found."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @GetMapping("/v1/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(
            @PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel elasticQueryServiceResponseModel = elasticQueryService.getDocumentById(id);
        LOG.debug("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(elasticQueryServiceResponseModel);
    }

    @Operation(summary = "Get document by id v2.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful response.", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))}),
            @ApiResponse(responseCode = "400", description = "Not Found."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @GetMapping("/v2/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(
            @PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel elasticQueryServiceResponseModel = elasticQueryService.getDocumentById(id);
        ElasticQueryServiceResponseModelV2 v2Model = getV2Model(elasticQueryServiceResponseModel);
        LOG.debug("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(v2Model);
    }

    @Operation(summary = "Get document by text.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful response.", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))}),
            @ApiResponse(responseCode = "400", description = "Not Found."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @PostMapping("/v1/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(
            @RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel) {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByText(
                elasticQueryServiceRequestModel.getText());
        LOG.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    private ElasticQueryServiceResponseModelV2 getV2Model(
            ElasticQueryServiceResponseModel elasticQueryServiceResponseModel) {
        ElasticQueryServiceResponseModelV2 build = ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(elasticQueryServiceResponseModel.getId()))
                .userId(elasticQueryServiceResponseModel.getUserId()).text(elasticQueryServiceResponseModel.getText())
                .createdAt(elasticQueryServiceResponseModel.getCreatedAt()).build();
        build.add(elasticQueryServiceResponseModel.getLinks());
        return build;
    }

}
