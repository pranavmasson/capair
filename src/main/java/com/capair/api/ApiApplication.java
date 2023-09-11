package com.capair.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@EnableJpaRepositories("com.capair.api.*")
@OpenAPIDefinition(
	info = @Info(
			title = "CapAir API Documentaiton",
			version = "1.0.0",
			description = "This API supports the backend of cap-air.com"
	)
)
@SpringBootApplication
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);	
	}
}
