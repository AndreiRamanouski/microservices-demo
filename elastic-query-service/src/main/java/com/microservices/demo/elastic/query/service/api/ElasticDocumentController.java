package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);
    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @PostAuthorize("hasPermission(returnObject, 'READ')")
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

    @PostAuthorize("hasPermission(#id, 'ElasticQueryServiceResponseModel', 'READ')")
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

    @PreAuthorize("hasRole('APP_USER_ROLE') || hasRole('APP_SUPER_USER_ROLE') || hasAnyAuthority('SCOPE_APP_USER_ROLE')")
    @PostAuthorize("hasPermission(returnObject, 'READ')")
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
