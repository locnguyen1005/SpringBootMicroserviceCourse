package com.example.demo.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.Lession_AccountDTO;
import com.example.demo.DTO.QuizDTO;
import com.example.demo.Service.Lession_Account_Service;
import com.example.demo.Service.QuizService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lessionAccount")
public class Lession_Account_Controller {
	@Autowired
	private Lession_Account_Service quizService;
	@GetMapping("/GetAll")
	public Flux<Lession_AccountDTO> getAllQuiz(){
		return quizService.getAllLession_Account();
	}
	@PostMapping("/create")
	public Mono<Lession_AccountDTO> createQuiz(@RequestBody Lession_AccountDTO quizDTO) throws IOException{
		return quizService.createLession_Account(quizDTO);
	}
//	@GetMapping("/GetAll/{productid}")
//	public Flux<Lession_AccountDTO> getAllQuizByproductId(@PathVariable Long productid){
//		return quizService.getAllQuizProductID(productid);
//	}
	@GetMapping("/getlessionaccount/{lessionid}/{accountid}")
	public Mono<Lession_AccountDTO> editLesssionAccount(@PathVariable Long lessionid ,@PathVariable Long accountid) throws IOException{
		return quizService.finđByIdLesion(lessionid,accountid);
	}
	@GetMapping("/getlessionproduct/{productid}/{accountid}")
	public Flux<Lession_AccountDTO> editLesssionproduct(@PathVariable Long productid ,@PathVariable Long accountid) throws IOException{
		return quizService.finđByIdProduct(productid,accountid);
	}
	
}
