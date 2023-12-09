package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Model.Answer;
import com.example.demo.Model.Comment;
import com.example.demo.Model.Quiz;

import com.example.demo.Repository.CommentRepository;

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
}
