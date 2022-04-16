package com.sellics.casestudy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
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
		
		
		LOGGER.info("Object from AWS S3 :{}", amazonS3.getObject(awsS3Bucket, fileName)
				.getObjectContent().toString());
		
		
	}

}
