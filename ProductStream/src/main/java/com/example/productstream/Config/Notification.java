package com.example.productstream.Config;

import com.example.commonservice.utils.ConstantCommon;
import com.example.productstream.Entity.ProductStream;
import com.example.productstream.Event.ProductStreamProducer;
import com.example.productstream.ProductStreamDTO.ProductStreamDTO;
import com.example.productstream.ProductStreamService.ProductService;
import com.example.productstream.Repository.ProductStreamRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Component
@Slf4j
@EnableScheduling
public class Notification {
    @Autowired
    Gson gson;
    @Autowired
    private ProductStreamProducer productProducer;
    @Autowired
    private ProductService productRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleTaskWithFixedDelay() {
        Flux<ProductStreamDTO> productEntityFlux = productRepository.getAllProductTrue();
        int dateofweek = LocalDateTime.now().getDayOfWeek().getValue();
        for (ProductStreamDTO element : productEntityFlux.toIterable()){
            for (Integer date : element.getDate()){
                if(date == dateofweek){
                    com.example.commonservice.Model.Notification notification = new com.example.commonservice.Model.Notification();
                    notification.setAccountid(element.getAccountid());
                    notification.setImage(element.getImage());
                    notification.setNotification("Hôm nay bạn có lịch dạy vào lúc "+element.getTime());
                    productProducer.send(ConstantCommon.NOTIFICATION,gson.toJson(notification));
                }

            }
        }
    }
}
