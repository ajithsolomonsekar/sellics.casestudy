package com.sellics.casestudy.config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.sellics.casestudy.service.S3ClientService;

@Component
public class Config implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(Config.class);

	@Value("${aws.s3.bucket}")
	private String awsS3Bucket;

	@Value("${aws.s3.object}")
	private String awsS3Object;

	@Autowired
	private S3ClientService amazonS3ClientService;

	@Override
	public void afterPropertiesSet() throws Exception {
		consumeS3Object(amazonS3ClientService);
	}

	@Async("threadPoolTaskExecutor")
	public S3ClientService consumeS3Object(S3ClientService amazonS3ClientService) {

		log.info("Started consuming S3 Object: {} from bucket: {}", awsS3Object, awsS3Bucket);
		CompletableFuture<String> asyncProcess = CompletableFuture
				.supplyAsync(() -> amazonS3ClientService.consumeObjectFromS3Bucket(awsS3Bucket, awsS3Object));

		asyncProcess.thenRunAsync(()-> log.info("Completed consuming S3 Object"));
		return amazonS3ClientService;
	}

}
