package com.example.demo.Service;


import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Common.CommonException;

import com.example.demo.DTO.QuizDTO;

import com.example.demo.Model.Quiz;
import com.example.demo.Repository.QuizRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class QuizService{
	@Autowired
	private QuizRepository quizRepository;
	@Autowired
	private ModelMapper modelMapper;
	public Flux<QuizDTO> getAllProduct(){
		return quizRepository.findAll().map(QuizDTO -> modelMapper.map(QuizDTO, QuizDTO.class)).switchIfEmpty(Mono.error(new CommonException("Quiz", "Quiz is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<Boolean> checkDuplicate(QuizDTO quizDTO){
		return quizRepository.findByQuestion(quizDTO.getQuestion())
				.map(quiz ->  Boolean.TRUE)
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<QuizDTO> createProduct(QuizDTO quizDTO) throws IOException{
		try {
			
			if(checkDuplicate(quizDTO).block().equals(Boolean.TRUE)) {
				return Mono.error(new CommonException(quizDTO.getQuestion(), "The course name already exists", HttpStatus.BAD_REQUEST));
			}
			else {
				
				return Mono.just(quizDTO)
						.map(productdto -> modelMapper.map(productdto, Quiz.class))
						.flatMap(product -> quizRepository.save(product))
						.map(productentity -> modelMapper.map(productentity, QuizDTO.class))
						.doOnSubscribe(dto -> log.info("susscess"));
			}
		} catch (Exception e) {
			return Mono.error(new CommonException(e.toString(), "", HttpStatus.BAD_REQUEST));
		}
	}
	public Mono<QuizDTO> finđById(Long productDTOID){
		return quizRepository.findById(productDTOID).map(ProductDTO -> modelMapper.map(ProductDTO, QuizDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Flux<QuizDTO> getAllQuizProductID(Long productid){
		return quizRepository.findByProductid(productid).map(ProductDTO -> modelMapper.map(ProductDTO, QuizDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
}
