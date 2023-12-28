package com.example.productservice;

import com.example.productservice.Config.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan({"com.example.productservice", "com.example.commonservice"})
public class ProductApplication {
	private static final Logger logger = LoggerFactory.getLogger(Notification.class);

	public static void main(String[] args) {
		
		SpringApplication.run(ProductApplication.class, args);
	}
	@Bean
	public WebClient.Builder webBuilder(){
		return WebClient.builder();
	}

}
