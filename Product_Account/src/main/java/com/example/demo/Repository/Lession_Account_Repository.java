package com.example.demo.Repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.demo.Model.Lession_Account;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Lession_Account_Repository extends R2dbcRepository<Lession_Account, Long>{
	@Query("SELECT * FROM payment WHERE  productid = :productId  ")
	Flux<Lession_Account> findByAccountid(long productId);
	
}
