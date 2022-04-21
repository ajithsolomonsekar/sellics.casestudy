package com.sellics.casestudy.service;

import java.util.concurrent.CompletableFuture;

public interface AmazonS3ClientService {

	CompletableFuture<Void> consumeObjectFromS3Bucket(String fileName);
}
