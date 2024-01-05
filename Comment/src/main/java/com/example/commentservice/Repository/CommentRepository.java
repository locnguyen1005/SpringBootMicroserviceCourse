package com.example.commentservice.Repository;

import com.example.commentservice.Model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String>{
	Flux<Comment> findByProductid(Long productid);
	Flux<Comment> findByLessionid(Long lessionid);
}
