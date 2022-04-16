package com.sellics.casestudy.service;

public interface AmazonS3ClientService {

	void consumeObjectFromS3Bucket(String fileName);
}
