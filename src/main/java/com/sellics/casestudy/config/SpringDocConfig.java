package com.sellics.casestudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocConfig {
	
	@Bean
	public OpenAPI springOpenAPI() {
		return new OpenAPI().info(new Info().title("Smart Ranking Index API")
				.description("SpringBoot application that ingests and processes file from S3 bucket, and presents the results through REST APIs that help retailers make sense of this data")
				.version("v1.0.0")
				);
	}

}
