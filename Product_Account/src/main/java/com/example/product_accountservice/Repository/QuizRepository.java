package com.example.product_accountservice.Repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;


import com.example.product_accountservice.Model.Quiz;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface QuizRepository extends R2dbcRepository<Quiz, Long> {
	Mono<Quiz> findByQuestion(String quesion);
	Flux<Quiz> findByProductid(long produtid);
	Flux<Quiz> findByLessionid(Long lessionid);
}
