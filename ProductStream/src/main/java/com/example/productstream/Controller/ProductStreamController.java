package com.example.productstream.Controller;

import com.example.productstream.ProductStreamDTO.ProductStreamDTO;

import com.example.productstream.ProductStreamService.ProductService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;


@RestController
@Slf4j
@RequestMapping("/ProductStream")
public class ProductStreamController {
    @Autowired
    private ProductService productService;
    @Autowired
    Gson gson;


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

    }

}
