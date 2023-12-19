package com.example.chatservice.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.chatservice.Model.ChatMessage;

import reactor.core.publisher.Flux;
@Repository
public interface ChatRepository extends ReactiveMongoRepository<ChatMessage, String>{
	Flux<ChatMessage> findByProductid(Long productid);
}
