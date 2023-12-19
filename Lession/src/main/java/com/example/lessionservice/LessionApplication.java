package com.example.lessionservice;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ComponentScan({"com.example.lessionservice", "com.example.commonservice"})
public class LessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LessionApplication.class, args);
	}
	@Bean
	public ModelMapper modelmapper() {
		ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
	}
	@Bean
	public WebClient.Builder webBuilder(){
		return WebClient.builder();
	}

}
