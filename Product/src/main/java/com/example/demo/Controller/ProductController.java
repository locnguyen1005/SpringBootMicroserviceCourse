package com.example.demo.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.DTO.ProductClient;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.Model.Account;
import com.example.demo.Model.Product;
import com.example.demo.Service.ProductService;
import com.example.demo.Utils.Constant;
import com.example.demo.utils.CommonValidate;
import com.example.demo.utils.ConstantCommon;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/product")
@Slf4j
@CrossOrigin(origins = "http://localhost:3006")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	Gson gson;
	
	
	@GetMapping("/getall")
	public Flux<ProductClient> getAllProduct(){
		return productService.getAllProduct();
	}
	@PostMapping("/createproduct")
	public ResponseEntity<Mono<ProductDTO>> createProduct(@RequestParam(value = "data") String product , @RequestParam(value = "file") MultipartFile file) throws JsonSyntaxException, IOException{
		log.info(product);
		InputStream inputStream = ProductController.class.getClassLoader().getResourceAsStream(Constant.JSON_Product);
		CommonValidate.jsonValidate(product, inputStream);
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(gson.fromJson(product, ProductDTO.class),file));
	}
	@GetMapping("/{id}")
	public ResponseEntity<Mono<ProductDTO>> detailProduct(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.finđById(id));
	}
	@PutMapping("/Edit/{id}")
	public ResponseEntity<Mono<ProductDTO>> edit(@PathVariable Long id ,@RequestParam(value = "data") String product , @RequestParam(value = "file") MultipartFile file){
		InputStream inputStream = ProductController.class.getClassLoader().getResourceAsStream(Constant.JSON_Product);
		CommonValidate.jsonValidate(product, inputStream);
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.editproduct(id,gson.fromJson(product, ProductDTO.class),file));
	}
	
}
