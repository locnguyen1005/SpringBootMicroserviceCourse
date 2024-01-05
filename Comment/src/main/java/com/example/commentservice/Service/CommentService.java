package com.example.commentservice.Service;

import com.example.commentservice.Model.Comment;
import com.example.commentservice.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private WebClient.Builder webBuilder;
	
	public Flux<Comment> getAllAnswer(){
		return  commentRepository.findAll();
	}
	public Mono<Comment> saveAnswer(Comment comment){	
		return commentRepository.save(comment);
	}
	public Flux<Comment> getAllAnswerbyid(Long productId){
		return  commentRepository.findByProductid(productId);
	}
	public Flux<Comment> getAlllesionid(Long productId){
		return  commentRepository.findByLessionid(productId);
	}
}
