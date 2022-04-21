package com.sellics.casestudy.validator;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SmartRankingIndexValidatorImpl implements SmartRankingIndexValidator {
	
	public Boolean validateSmartRankingIndexRequest(String asin, String keyword, List<String> status) {
		Boolean isValidRequest = true;
		
		if(asin.isBlank()) {
			status.add("ASIN cannot be Blank");
			isValidRequest = false;
		}
		
		if(keyword.isBlank()) {
			status.add("Keyword cannot be Blank");
			isValidRequest = false;
		}
		return isValidRequest;
	}

}
