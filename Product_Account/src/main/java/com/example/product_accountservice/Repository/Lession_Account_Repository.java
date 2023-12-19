package com.example.product_accountservice.Repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.product_accountservice.Model.Lession_Account;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Lession_Account_Repository extends R2dbcRepository<Lession_Account, Long>{
	@Query("SELECT * FROM payment WHERE  productid = :productId  ")
	Flux<Lession_Account> findByAccountid(Long productId);
	@Query("SELECT * FROM lessionaccount WHERE  accountId = :accountId AND lessionId = :lessionId ")
	Mono<Lession_Account> findByAccountLession(Long lessionId , Long accountId);
	@Query("SELECT * FROM lessionaccount WHERE  productid = :productid AND accountId = :accountId ")
	Flux<Lession_Account> findByAccountLessionByProduct(Long productid , Long accountId);
}
