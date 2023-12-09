package com.example.demo.Repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;


import com.example.demo.Entity.LessionEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface LessionRepository extends R2dbcRepository<LessionEntity, Long>{
	Mono<LessionEntity> findByTitle(String email);
	Flux<LessionEntity> findByProductId(Long productId);
}
