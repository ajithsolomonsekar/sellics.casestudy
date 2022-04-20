package com.sellics.casestudy.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sellics.casestudy.entity.ProductData;
import com.sellics.casestudy.repository.ProductDataRepository;
import com.sellics.casestudy.service.AmazonS3ClientService;

@Service
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {

	public static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3Client;

	@Autowired
	private String awsS3Bucket;

	@Autowired
	private ProductDataRepository productDataRepository;

	@Override
	public void consumeObjectFromS3Bucket(String fileName) {
		LOGGER.info("test:{}, :{}", awsS3Bucket, fileName);

		S3Object objectPortion = null;
		
		ObjectMetadata s3ObjectMetadata = amazonS3Client.getObjectMetadata(awsS3Bucket, fileName);
		long startByte = 0, endByte = 1000, totalByte = s3ObjectMetadata.getContentLength();
		
		while(totalByte >= endByte-1) {
			GetObjectRequest rangeObjectRequest = new GetObjectRequest(awsS3Bucket, fileName)
					.withRange(startByte, endByte-1);
			startByte = endByte;
			objectPortion = amazonS3Client.getObject(rangeObjectRequest);
			processS3InputStream(objectPortion.getObjectContent());
		}
		
		/*
		 * var s3InputStream = amazonS3Client.getObject(awsS3Bucket,
		 * fileName).getObjectContent(); var mapper = new CsvMapper(); var schema =
		 * CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
		 * 
		 * MappingIterator<ProductData> readData; try { readData =
		 * mapper.readerWithTypedSchemaFor(ProductData.class).with(schema).readValues(
		 * s3InputStream); } catch (IOException e) {
		 * LOGGER.error("Error occured while parsing CSV to Data object"); return; }
		 * List<ProductData> productDataList = new ArrayList<>();
		 * 
		 * var modelMapper = new ModelMapper();
		 * 
		 * while (readData.hasNext()) { var productDataFromS3 = readData.next(); var
		 * productDataEntity = modelMapper.map(productDataFromS3, ProductData.class);
		 * productDataRepository.save(productDataEntity); //
		 * productDataList.add(productDataEntity);
		 * 
		 * } // productDataRepository.saveAll(productDataList);
		 */
	}

	private void processS3InputStream(InputStream input) {

		var mapper = new CsvMapper();
		var schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');

		MappingIterator<ProductData> readData;
		try {
			readData = mapper.readerWithTypedSchemaFor(ProductData.class).with(schema).readValues(input);

			var modelMapper = new ModelMapper();

			while (readData.hasNext()) {
				var productDataFromS3 = readData.next();
				var productDataEntity = modelMapper.map(productDataFromS3, ProductData.class);
				productDataRepository.save(productDataEntity);
			}
		} catch (IOException e) {
			LOGGER.error("Error occured while parsing CSV to Data object");
		}
	}

}
