package com.sellics.casestudy.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sellics.casestudy.entity.ProductData;
import com.sellics.casestudy.model.TimeSeriesResponse;
import com.sellics.casestudy.repository.ProductDataRepository;
import com.sellics.casestudy.service.ProductDataService;

@Service
public class ProductDataServiceImpl implements ProductDataService {

	@Autowired
	private ProductDataRepository productDataRepository;
	
	@Override
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndex(String asin, String keyword) {
		
		var productDataList = productDataRepository.findByAsinAndKeyword(asin, keyword);
		
		var response = new TimeSeriesResponse();
		
		if(productDataList.isPresent()) {
			var list = productDataList.get();
			
			SortedSet<String[]> observationResponse = new TreeSet<>();
			if(!list.isEmpty()) {
				list.stream()
//				.distinct()
				.sorted(Comparator.comparingLong(ProductData::getTimestamp))
				.forEach(data -> {
					String[] observation = new String[2];
					var timestamp = Instant.ofEpochMilli(data.getTimestamp())
							.atZone(ZoneId.systemDefault()).toLocalDateTime();
					observation[0]=timestamp.toString();
					observation[1]=data.getRank().toString();
					observationResponse.add(observation);
				});
				response.setObservations(observationResponse);
				response.setAsin(asin);
				response.setKeyword(keyword);
//				response.setHttpStatus(HttpStatus.OK);
				return new ResponseEntity<TimeSeriesResponse>(response, HttpStatus.OK);
			}
		}
		response.setHttpStatus(HttpStatus.NOT_FOUND);
		response.setDeveloperMessage("No Product found for given ASIN & Keyword");
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		
	}

}
