package com.sellics.casestudy.validator;

import java.util.List;

public interface SmartRankingIndexValidator {
	
	Boolean validateSmartRankingIndexRequest(String asin, String keyword, List<String> status);

}
