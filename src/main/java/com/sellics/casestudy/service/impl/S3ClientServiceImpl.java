package com.sellics.casestudy.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sellics.casestudy.entity.ProductData;
import com.sellics.casestudy.repository.ProductDataRepository;
import com.sellics.casestudy.service.S3ClientService;
import com.sellics.casestudy.util.Constants;

@Service
public class S3ClientServiceImpl implements S3ClientService {

	public static final Logger log = LoggerFactory.getLogger(S3ClientServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3Client;

	@Autowired
	private ProductDataRepository productDataRepository;

	@Override
	@Async("threadPoolTaskExecutor")
	public String consumeObjectFromS3Bucket(String bucketName, String fileName) {
		long startTime = System.currentTimeMillis();
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);

		try {
			processS3InputStream(amazonS3Client.getObject(getObjectRequest).getObjectContent());
			log.info("Total records count: {} and Time taken: {}", productDataRepository.count(),
					System.currentTimeMillis() - startTime);
		} catch (Exception ex) {
			log.error("Error occured while consuming s3 object: {}", ex.getMessage());
			return Constants.ERR_RESPONSE_4;
		}
		log.info("Successfully consumed S3 object: {}", fileName);
		return Constants.SUCCESS_RESPONSE;

	}

	private void processS3InputStream(S3ObjectInputStream s3InputStream) {

		var mapper = new CsvMapper();
		var schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');

		MappingIterator<ProductData> readData;
		try {
			readData = mapper.readerWithTypedSchemaFor(ProductData.class).with(schema).readValues(s3InputStream);

			while (readData.hasNext()) {
				var productDataEntity = readData.next();
				productDataRepository.save(productDataEntity);
			}
		} catch (IOException e) {
			log.error("Error occured while parsing CSV to Data object");
		}
	}

}
