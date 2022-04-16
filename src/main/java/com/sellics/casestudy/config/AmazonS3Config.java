package com.sellics.casestudy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {

	@Value("${aws.credentials.accessKey}")
    private String accessKey;

    @Value("${aws.credentials.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;
    
    @Value("${aws.s3.bucket}")
    private String awsS3Bucket;
    
    @Value("${aws.s3.object}")
    private String awsS3Object;
    
	@Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentials awsCredentials) {
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(Regions.fromName(region))
        .build();
    }
    
    @Bean(name = "awsS3Bucket")
    public String getAWSS3Bucket() {
        return awsS3Bucket;
    }
    
    @Bean(name = "awsS3Object")
    public String getAWSS3Object() {
        return awsS3Object;
    }
}