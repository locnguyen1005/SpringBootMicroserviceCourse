package com.example.product_accountservice.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.commonservice.Common.CommonException;
import com.example.product_accountservice.Model.AccountRegister;
import com.example.product_accountservice.Model.Account_Product;

import com.example.product_accountservice.Repository.Product_Account_Reopository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class Product_Account_Service {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Product_Account_Reopository repository;
	@Autowired
	private WebClient.Builder webBuilder;
	
	public Flux<Account_Product> getAllProduct(){
		return repository.findAll().map(Product_account -> modelMapper.map(Product_account, Account_Product.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Flux<AccountRegister> getAllProductAccountRegistered(Long id){
		
		return repository.findByAccountId(id).switchIfEmpty(Flux.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<AccountRegister> registerProduct(AccountRegister accountRegister){
		log.info("register service true");
		return repository.save(accountRegister);
	}
	public Flux<AccountRegister> getAllAccount(Long id){
		
		return repository.findByProductId(id).switchIfEmpty(Flux.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
}
