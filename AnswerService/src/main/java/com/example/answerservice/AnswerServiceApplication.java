package com.example.answerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.example.answerservice", "com.example.commonservice"})
public class AnswerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnswerServiceApplication.class, args);
	}
	@Bean
	public WebClient.Builder webBuilder(){
		return WebClient.builder();
	}
}
