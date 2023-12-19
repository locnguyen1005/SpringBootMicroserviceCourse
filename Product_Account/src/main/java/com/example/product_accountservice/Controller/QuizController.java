package com.example.product_accountservice.Controller;

import java.io.IOException;

import com.example.product_accountservice.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_accountservice.DTO.QuizDTO;

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
	@GetMapping("/GetAllByLessionId/{lessionid}")
	public Flux<QuizDTO> getAllQuizByLessionId(@PathVariable Long lessionid){
		return quizService.getAllQuizLessionID(lessionid);
	}
	
}
