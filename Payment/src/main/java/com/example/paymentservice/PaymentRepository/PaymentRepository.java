package com.example.paymentservice.PaymentRepository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.paymentservice.PaymentEntity.PaymentEntity;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentRepository extends R2dbcRepository<PaymentEntity , Long> {
	@Query("SELECT * FROM payment WHERE accountid = :accountId AND productid = :productId  ")
	Mono<PaymentEntity> findByAccountid(long accountId , long productId);
	@Query("SELECT * FROM payment WHERE accountid = :accountId")
	Flux<PaymentEntity> findByAccountid(long accountId );
}
