package com.sellics.casestudy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellics.casestudy.config.ConsumeS3ObjectComponent;
import com.sellics.casestudy.model.TimeSeriesResponse;
import com.sellics.casestudy.service.ProductDataService;
import com.sellics.casestudy.service.S3ClientService;
import com.sellics.casestudy.validator.SmartRankingIndexRequestValidator;

@RestController
@RequestMapping("/v1")
public class ProductDataController {

	@Autowired
	private ProductDataService productDataService;
	
	@Autowired
	private ConsumeS3ObjectComponent consumeS3ObjectComponent;
	
	@Autowired
	private S3ClientService amazonS3ClientService;

	@Autowired
	private SmartRankingIndexRequestValidator smartRankingIndexValidator;

	private static final Logger log = LoggerFactory.getLogger(ProductDataController.class);

	@GetMapping(path = "/smart_raking_index", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndex(@Valid @RequestParam("asin") String asin,
			@Valid @RequestParam("keyword") String keyword) {

		log.info("Request asin: {} with keyword: {}", asin, keyword);
		List<String> validation = new ArrayList<>();
		if (!smartRankingIndexValidator.validateSmartRankingIndexRequest(asin, keyword, validation)) {
			return createBadRequestResponse(validation);
		}
		return productDataService.getSmartRankingIndex(asin, keyword);

	}

	@GetMapping(path = "/smart_ranking_index_keyword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndexForKeyword(
			@Valid @RequestParam("keyword") String keyword) {

		log.info("Request keyword: {}", keyword);
		List<String> validation = new ArrayList<>();
		if (!smartRankingIndexValidator.validateSmartRankingIndexRequestForKeyword(keyword, validation)) {
			return createBadRequestResponse(validation);
		}
		return productDataService.getSmartRankingIndexForKeyword(keyword);

	}

	@GetMapping(path = "/smart_ranking_index_asin", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndexForASIN(@Valid @RequestParam("asin") String asin) {

		log.info("Request ASIN: {}", asin);
		List<String> validation = new ArrayList<>();
		if (!smartRankingIndexValidator.validateSmartRankingIndexRequestForASIN(asin, validation)) {
			return createBadRequestResponse(validation);
		}
		return productDataService.getSmartRankingIndexForASIN(asin);

	}

	private ResponseEntity<TimeSeriesResponse> createBadRequestResponse(List<String> status) {
		TimeSeriesResponse response = new TimeSeriesResponse();
		response.setHttpStatus(HttpStatus.BAD_REQUEST);
		response.setDeveloperMessage(String.join(",", status));
		return new ResponseEntity<TimeSeriesResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(path="/consumes3object")
	public ResponseEntity<String> consumeS3Object(){
		consumeS3ObjectComponent.consumeS3Object(amazonS3ClientService);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

}
