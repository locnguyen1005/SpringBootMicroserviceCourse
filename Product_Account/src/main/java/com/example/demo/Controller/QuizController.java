package com.example.demo.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.example.demo.DTO.QuizDTO;
import com.example.demo.Service.QuizService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/Quiz")
public class QuizController {
	@Autowired
	private QuizService quizService;
	@GetMapping("/GetAll")
	public Flux<QuizDTO> getAllQuiz(){
		return quizService.getAllProduct();
	}
	@PostMapping("/create")
	public Mono<QuizDTO> createQuiz(@RequestBody QuizDTO quizDTO) throws IOException{
		return quizService.createProduct(quizDTO);
	}
	@GetMapping("/GetAll/{productid}")
	public Flux<QuizDTO> getAllQuizByproductId(@PathVariable Long productid){
		return quizService.getAllQuizProductID(productid);
	}
}
