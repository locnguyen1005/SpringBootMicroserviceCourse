package com.example.productservice.Config;

import com.example.productservice.Entity.ProductEntity;
import com.example.productservice.Repository.ProductRepository;
import com.example.productservice.event.ProductProducer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.Flushable;


@Component
@Slf4j
@EnableScheduling
public class Notification {
    private static final Logger logger = LoggerFactory.getLogger(Notification.class);
    @Autowired
    private ProductProducer productProducer;
    @Autowired
    private ProductRepository productRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleTaskWithFixedDelay() {
        Flux<ProductEntity> productEntityFlux = productRepository.findAll();
    }
}
