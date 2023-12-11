package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Answer;

import reactor.core.publisher.Flux;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer, String>{
	Flux<Answer> findByLessionid(Long lessionid);
	
	@Query("{ 'accountid' : ?0, 'lessionid' : ?1 }")
	Flux<Answer> findbyLessionAccountid(Long accountid , Long lessionid);
	
}


