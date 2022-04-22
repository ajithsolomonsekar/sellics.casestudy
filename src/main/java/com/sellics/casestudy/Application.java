package com.sellics.casestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	executor.setMaxPoolSize(5);
    	executor.setWaitForTasksToCompleteOnShutdown(true);
    	executor.setThreadNamePrefix("amazon-s3-client-thread-");
    	executor.initialize();
    	return executor;
    }

}
