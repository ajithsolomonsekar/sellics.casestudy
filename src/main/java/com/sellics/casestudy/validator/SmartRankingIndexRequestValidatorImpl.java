package com.sellics.casestudy.validator;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SmartRankingIndexRequestValidatorImpl implements SmartRankingIndexRequestValidator {

	public Boolean validateSmartRankingIndexRequest(String asin, String keyword, List<String> validation) {
		return !validateASIN(asin, validation) && !validateKeyword(keyword, validation);
	}
	
	public Boolean validateSmartRankingIndexRequestForKeyword(String keyword, List<String> status) {
		return !validateKeyword(keyword, status);
	}
	
	public Boolean validateSmartRankingIndexRequestForASIN(String asin, List<String> status) {
		return !validateASIN(asin, status);
	}

	private Boolean validateASIN(String asin, List<String> validation) {
		boolean isBlank = asin.isBlank();
		if(isBlank) {
			validation.add("ASIN cannot be Blank");
		}
		return isBlank;
	}
	
	private Boolean validateKeyword(String keyword, List<String> validation) {
		boolean isBlank = keyword.isBlank();
		if(isBlank) {
			validation.add("Keyword cannot be Blank");
		}
		return isBlank;
	}

}
