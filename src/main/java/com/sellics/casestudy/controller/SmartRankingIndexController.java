package com.sellics.casestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sellics.casestudy.service.AmazonS3ClientService;

@RestController
@RequestMapping("/v1")
public class SmartRankingIndexController {
	
	@Autowired
	private AmazonS3ClientService amazonS3ClientService;
	
	@Autowired
	private String awsS3Object;
	
	@GetMapping("/resource1")
	public ResponseEntity<String> getIRForASINForCKeyword(){
		
		amazonS3ClientService.consumeObjectFromS3Bucket(awsS3Object);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
