package com.sellics.casestudy.service;

import org.springframework.http.ResponseEntity;

import com.sellics.casestudy.model.TimeSeriesResponse;

public interface ProductDataService {

	ResponseEntity<TimeSeriesResponse> getSmartRankingIndex(String asin, String keyword);

	ResponseEntity<TimeSeriesResponse> getSmartRankingIndexForKeyword(String keyword);

	ResponseEntity<TimeSeriesResponse> getSmartRankingIndexForASIN(String asin);

}
