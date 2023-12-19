package com.example.product_accountservice.Service;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.commonservice.Common.CommonException;
import com.example.product_accountservice.DTO.Lession_AccountDTO;
import com.example.product_accountservice.DTO.QuizDTO;
import com.example.product_accountservice.Model.Lession_Account;
import com.example.product_accountservice.Repository.Lession_Account_Repository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class Lession_Account_Service {
	@Autowired
	private Lession_Account_Repository quizRepository;
	@Autowired
	private ModelMapper modelMapper;
	public Flux<Lession_AccountDTO> getAllLession_Account(){
		return quizRepository.findAll().map(QuizDTO -> modelMapper.map(QuizDTO, Lession_AccountDTO.class)).switchIfEmpty(Mono.error(new CommonException("Quiz", "Quiz is empty", HttpStatus.BAD_REQUEST)));
	}

	public Mono<Lession_AccountDTO> createLession_Account(Lession_AccountDTO quizDTO) throws IOException{
		try {
				
				return Mono.just(quizDTO)
						.map(productdto -> modelMapper.map(productdto, Lession_Account.class))
						.flatMap(product -> quizRepository.save(product))
						.map(productentity -> modelMapper.map(productentity, Lession_AccountDTO.class))
						.doOnSubscribe(dto -> log.info("susscess"));

		} catch (Exception e) {
			return Mono.error(new CommonException(e.toString(), "", HttpStatus.BAD_REQUEST));
		}
	}
	public Mono<QuizDTO> finđById(Long productDTOID){
		return quizRepository.findById(productDTOID).map(ProductDTO -> modelMapper.map(ProductDTO, QuizDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<Lession_AccountDTO> finđByIdLesion(Long accountid , Long lessionid){
		return quizRepository.findByAccountLession(lessionid,accountid).map(ProductDTO -> modelMapper.map(ProductDTO, Lession_AccountDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
//	public Flux<QuizDTO> getAllQuizProductID(Long productid){
//		return quizRepository.findByProductid(productid).map(ProductDTO -> modelMapper.map(ProductDTO, QuizDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
//	}
	public Flux<Lession_AccountDTO> finđByIdProduct(Long productid , Long accountid){
		return quizRepository.findByAccountLessionByProduct(productid,accountid).map(ProductDTO -> modelMapper.map(ProductDTO, Lession_AccountDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
}
