package com.sellics.casestudy.service;

public interface S3ClientService {

	String consumeObjectFromS3Bucket(String bucketName, String fileName);
}
