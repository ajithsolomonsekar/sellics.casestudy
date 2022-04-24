package com.sellics.casestudy.validator;

import java.util.List;

public interface SmartRankingIndexRequestValidator {

	Boolean validateSmartRankingIndexRequest(String asin, String keyword, List<String> status);

	Boolean validateSmartRankingIndexRequestForKeyword(String keyword, List<String> status);

	Boolean validateSmartRankingIndexRequestForASIN(String asin, List<String> status);

}
