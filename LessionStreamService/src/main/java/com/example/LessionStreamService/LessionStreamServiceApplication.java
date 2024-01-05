package com.example.LessionStreamService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.example.LessionStreamService", "com.example.commonservice"})
public class LessionStreamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LessionStreamServiceApplication.class, args);
	}

}
