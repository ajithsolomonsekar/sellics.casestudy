package com.sellics.casestudy.service.impl;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
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
	@Async("threadPoolTaskExecutor")
	public CompletableFuture<Void> consumeObjectFromS3Bucket(String fileName) {
	
		GetObjectRequest getObjectRequest = new GetObjectRequest(awsS3Bucket, fileName);
		
		long startTime = System.currentTimeMillis();
			CompletableFuture<Void> process1 = CompletableFuture.runAsync(
					()->processS3InputStream(amazonS3Client.getObject(getObjectRequest).getObjectContent()));
			
		LOGGER.info("Total time taken: {}", System.currentTimeMillis()-startTime);
		process1.join();
		return CompletableFuture.completedFuture(null);
	}
	
	private void processS3InputStream(S3ObjectInputStream s3InputStream) {
		
		var mapper = new CsvMapper();
		var schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');

		MappingIterator<ProductData> readData;
		try {
			readData = mapper.readerWithTypedSchemaFor(ProductData.class)
					.with(schema).readValues(s3InputStream);

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
