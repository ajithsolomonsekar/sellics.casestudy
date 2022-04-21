package com.sellics.casestudy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellics.casestudy.model.TimeSeriesResponse;
import com.sellics.casestudy.service.AmazonS3ClientService;
import com.sellics.casestudy.service.ProductDataService;
import com.sellics.casestudy.validator.SmartRankingIndexValidator;

@RestController
@RequestMapping("/v1")
public class ProductDataController {
	
	@Autowired
	private AmazonS3ClientService amazonS3ClientService;
	
	@Autowired
	private String awsS3Object;
	
	@Autowired
	private ProductDataService productDataService;
	
	@Autowired
	private SmartRankingIndexValidator smartRankingIndexValidator;
	
	private static final Logger log = LoggerFactory.getLogger(ProductDataController.class);

	@GetMapping("/resource1")
	public ResponseEntity<String> getIRForASINForCKeyword(){
		
		amazonS3ClientService.consumeObjectFromS3Bucket(awsS3Object);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(path = "/resource2")
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndex(@Valid @RequestParam("asin")String asin,
			@Valid @RequestParam("keyword")String keyword){
		
		log.info("Request asin: {} with keyword: {}", asin, keyword);
		List<String> status = new ArrayList<>();
		if(!smartRankingIndexValidator.validateSmartRankingIndexRequest(asin, keyword, status)) {
			TimeSeriesResponse response = new TimeSeriesResponse();
			response.setHttpStatus(HttpStatus.BAD_REQUEST);
			response.setDeveloperMessage(String.join(",", status));
			return new ResponseEntity<TimeSeriesResponse>(response, HttpStatus.BAD_REQUEST);
		}
		return productDataService.getSmartRankingIndex(asin, keyword);
		
	}
	
}
