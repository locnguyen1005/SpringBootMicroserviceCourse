package com.example.productservice.Repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.productservice.Entity.ProductEntity;

import reactor.core.publisher.Mono;

public interface ProductRepository extends R2dbcRepository<ProductEntity, Long>{
	Mono<ProductEntity> findByName(String name);
}
