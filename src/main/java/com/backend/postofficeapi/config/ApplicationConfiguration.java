package com.backend.postofficeapi.config;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class ApplicationConfiguration {
	@Bean
	public ExecutorService gtExecutorService() {
		return Executors.newFixedThreadPool(5);
	}

    @Value("${openapi.local.url}")
    private String localUrl;

    @Value("${openapi.dev.url}")
    private String devUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("All India Post Office API")
                        .version("1.0")
                        .description("API for managing and retrieving post office information"))
                .servers(List.of(
                        new Server().url(localUrl).description("Local environment"),
                        new Server().url(devUrl).description("Development environment")
                ));
    }
}

