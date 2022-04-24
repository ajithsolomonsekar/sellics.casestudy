package com.sellics.casestudy.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.math.Quantiles;
import com.sellics.casestudy.entity.ProductData;
import com.sellics.casestudy.model.ProductDataDTO;
import com.sellics.casestudy.model.TimeSeriesResponse;
import com.sellics.casestudy.model.TimeSeriesResponseData;
import com.sellics.casestudy.model.TimeSeriesResponseHeader;
import com.sellics.casestudy.model.TimeSeriesResponseHeaderColumn;
import com.sellics.casestudy.repository.ProductDataRepository;
import com.sellics.casestudy.service.ProductDataService;

@Service
public class ProductDataServiceImpl implements ProductDataService {

	private static final Logger log = LoggerFactory.getLogger(ProductDataServiceImpl.class);

	@Autowired
	private ProductDataRepository productDataRepository;

	@Override
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndex(String asin, String keyword) {

		var optionalProductDataList = productDataRepository.findByAsinAndKeyword(asin, keyword);

		var response = new TimeSeriesResponse();

		if (optionalProductDataList.isPresent()) {
			var productDataList = optionalProductDataList.get();

			if (!productDataList.isEmpty()) {
				response.setDocType("jts");
				response.setVersion("1.0");
				
				Function<ProductData, ProductDataDTO> entityToDTO = product -> new ModelMapper().map(product,
						ProductDataDTO.class);

				List<ProductDataDTO> productDataDTOList = productDataList.stream().map(entityToDTO).distinct()
						.sorted(Comparator.comparingLong(ProductDataDTO::getTimestamp)).collect(Collectors.toList());

				List<TimeSeriesResponseData> data = new ArrayList<>();
				productDataDTOList.forEach(product -> {
					var timeSeriesData = new TimeSeriesResponseData();
					var timestamp = toLocalDateTime(product.getTimestamp());
					timeSeriesData.setTimestamp(timestamp.toString());
					timeSeriesData.setRank(product.getRank());

					data.add(timeSeriesData);
				});

				response.setData(data);
				var header = new TimeSeriesResponseHeader();
				setResponseHeader(header, data, "NONE");
				response.setHeader(header);

				return new ResponseEntity<TimeSeriesResponse>(response, HttpStatus.OK);
			}
		}
		response.setHttpStatus(HttpStatus.NOT_FOUND);
		response.setDeveloperMessage("No Product details found for given ASIN & Keyword");
		log.error("No Product details found for given ASIN: {} & Keyword: {}", asin, keyword);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}

	@Override
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndexForKeyword(String keyword) {

		var optionalProductDataList = productDataRepository.findByKeyword(keyword);
		var response = new TimeSeriesResponse();

		if (optionalProductDataList.isPresent() && !optionalProductDataList.get().isEmpty()) {
			return processProductData(optionalProductDataList.get(), response);
		}

		response.setHttpStatus(HttpStatus.NOT_FOUND);
		response.setDeveloperMessage("No Product details found for given Keyword");

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<TimeSeriesResponse> getSmartRankingIndexForASIN(String asin) {

		var optionalProductDataList = productDataRepository.findByAsin(asin);
		var response = new TimeSeriesResponse();

		if (optionalProductDataList.isPresent() && !optionalProductDataList.get().isEmpty()) {
			return processProductData(optionalProductDataList.get(), response);
		}

		response.setHttpStatus(HttpStatus.NOT_FOUND);
		response.setDeveloperMessage("No Product details found for given ASIN");

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}

	private ResponseEntity<TimeSeriesResponse> processProductData(List<ProductData> productDataList,
			TimeSeriesResponse response) {

		response.setDocType("jts");
		response.setVersion("1.0");

		List<TimeSeriesResponseData> data = new ArrayList<>();
		productDataList.stream().map(ProductData::getTimestamp).distinct().sorted().forEach(timestamp -> {

			var rankList = productDataList.stream().filter(product -> timestamp.equals(product.getTimestamp()))
					.map(ProductData::getRank).collect(Collectors.toList());

			Integer aggregatedRank = findMedian(rankList);
			String localDataTime = toLocalDateTime(timestamp);
			log.debug("Aggreagated Rank for Timestamp: {} is {} ", aggregatedRank, localDataTime);

			var timeSeriesData = new TimeSeriesResponseData();
			timeSeriesData.setTimestamp(localDataTime);
			timeSeriesData.setRank(aggregatedRank);

			data.add(timeSeriesData);
		});

		response.setData(data);
		var header = new TimeSeriesResponseHeader();
		setResponseHeader(header, data, "MEDIAN");
		response.setHeader(header);

		return new ResponseEntity<TimeSeriesResponse>(response, HttpStatus.OK);

	}

	private void setResponseHeader(TimeSeriesResponseHeader header, List<TimeSeriesResponseData> responseDataList,
			String aggregateType) {
		header.setStartTime(responseDataList.get(0).getTimestamp());
		int count = responseDataList.size();
		header.setEndTime(responseDataList.get(count - 1).getTimestamp());
		header.setRecordCount(count);
		header.setColumns(Arrays.asList(setCommonAttributes(aggregateType)));
	}

	private TimeSeriesResponseHeaderColumn setCommonAttributes(String aggregateType) {
		var column = new TimeSeriesResponseHeaderColumn();
		column.setAggregate(aggregateType);
		column.setDataType("NUMBER");
		column.setFormat("0");
		column.setName("Rank");
		column.setRenderType("VALUE");
		return column;
	}

	private String toLocalDateTime(Long timestamp) {
		return Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime().toString();
	}

	private Integer findMedian(List<Integer> rankList) {
		Double d = Quantiles.median().compute(rankList);
		return d.intValue();
	}

}
