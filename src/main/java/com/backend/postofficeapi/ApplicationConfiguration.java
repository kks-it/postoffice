package com.backend.postofficeapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
	@Bean
	public ExecutorService gtExecutorService() {
		return Executors.newFixedThreadPool(5);
	}
}
