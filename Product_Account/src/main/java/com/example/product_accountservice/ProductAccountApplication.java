package com.example.product_accountservice;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableDiscoveryClient
public class ProductAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductAccountApplication.class, args);
	}
	
	@Bean
	public WebClient.Builder webBuilder(){
		return WebClient.builder();
	}
	@Bean
	public ModelMapper modelmapper() {
		ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
	}
	
}
