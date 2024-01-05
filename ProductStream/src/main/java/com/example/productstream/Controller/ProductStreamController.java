package com.example.productstream.Controller;

import com.example.commonservice.utils.ConstantCommon;
import com.example.productstream.Event.ProductStreamProducer;
import com.example.productstream.ProductStreamDTO.ProductStreamDTO;

import com.example.productstream.ProductStreamService.ProductService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
@Slf4j
@RequestMapping("/ProductStream")
@CrossOrigin(origins = "http://localhost:3006")
public class ProductStreamController {
    @Autowired
    private ProductService productService;
    @Autowired
    Gson gson;
    @Autowired
    private ProductStreamProducer productProducer;

    @GetMapping("/getall")
    public Flux<ProductStreamDTO> getAllProduct(){
        return productService.getAllProductTrue();
    }
    @PostMapping("/createproduct")
    public ResponseEntity<Mono<ProductStreamDTO>> createProduct(@RequestParam(value = "data") String product , @RequestParam(value = "file") MultipartFile file) throws JsonSyntaxException, IOException {
        log.info(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(gson.fromJson(product, ProductStreamDTO.class),file));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Mono<ProductStreamDTO>> detailProduct(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.finđById(id));
    }
    @PutMapping("/Edit/{id}")
    public ResponseEntity<Mono<ProductStreamDTO>> edit(@PathVariable String id ,@RequestParam(value = "data" , required = false) String product , @RequestParam(value = "file" , required = false) MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.editproduct(id,gson.fromJson(product, ProductStreamDTO.class),file));
    }
    @GetMapping("/test")
    public void test(){
        com.example.commonservice.Model.Notification notification = new com.example.commonservice.Model.Notification();
        notification.setAccountid(1L);
        notification.setImage("loc");
        notification.setNotification("Hôm nay bạn có lịch dạy vào lúc ");
        String status1 = productProducer.send(ConstantCommon.NOTIFICATION,"gson.toJson(notification)").block();
    }

}
