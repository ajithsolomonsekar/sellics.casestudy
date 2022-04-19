package com.sellics.casestudy.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sellics.casestudy.model.Product;
import com.sellics.casestudy.service.AmazonS3ClientService;

@Service
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private String awsS3Bucket;
	
	
	@Override
	public void consumeObjectFromS3Bucket(String fileName) {
		LOGGER.info("test:{}, :{}", awsS3Bucket, fileName);
/*
		GetObjectRequest request = new GetObjectRequest(awsS3Bucket, fileName);
        File newFile = new File("C:\\Users\\JARVIS\\Downloads\\foo.csv");

        amazonS3.getObject(request, newFile);*/
		
		
		S3ObjectInputStream s3InputStream = amazonS3.getObject(awsS3Bucket, fileName).getObjectContent();
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
		
		MappingIterator<Product> readData;
		try {
			readData = mapper.readerWithTypedSchemaFor(Product.class)
					.with(schema)
					.readValues(s3InputStream);
		} catch (IOException e) {
			LOGGER.error("Error occured while parsing CSV to Data object");
			return;
		}
		List<Product> dataList = new ArrayList<>();
		while(readData.hasNext()) {
			Product d = readData.next();
			dataList.add(d);
		}

	}

}
