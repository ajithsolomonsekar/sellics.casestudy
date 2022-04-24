package com.sellics.casestudy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${aws.credentials.accessKey}")
	private String accessKey;

	@Value("${aws.credentials.secretKey}")
	private String secretKey;

	@Value("${aws.region}")
	private String region;

	@Bean
	public AmazonS3 amazonS3Client() {

		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);

		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
				.withRegion(Regions.fromName(region)).build();
	}

}