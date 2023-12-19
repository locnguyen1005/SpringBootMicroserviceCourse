package com.example.answerservice.Repository;

import com.example.answerservice.Model.Answer;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<com.example.answerservice.Model.Answer, String>{
	Flux<com.example.answerservice.Model.Answer> findByLessionid(Long lessionid);
	
	@Query("{ 'accountid' : ?0, 'lessionid' : ?1 }")
	Flux<Answer> findbyLessionAccountid(Long accountid , Long lessionid);
	
}


