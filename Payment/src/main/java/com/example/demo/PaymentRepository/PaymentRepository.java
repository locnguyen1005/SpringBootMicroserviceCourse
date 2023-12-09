package com.example.demo.PaymentRepository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.demo.PaymentDTO.PaymentDTO;
import com.example.demo.PaymentEntity.PaymentEntity;


import reactor.core.publisher.Mono;

public interface PaymentRepository extends R2dbcRepository<PaymentEntity , Long> {
	@Query("SELECT * FROM payment WHERE accountid = :accountId AND productid = :productId  ")
	Mono<PaymentEntity> findByAccountid(long accountId , long productId);
}
