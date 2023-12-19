package com.example.answerservice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.answerservice.Model.Answer;
import com.example.commonservice.Model.Quiz;
import com.example.answerservice.Repository.AnswerRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AnswerService {
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private WebClient.Builder webBuilder;
	
	public Flux<Answer> getAllAnswer(){
		return  answerRepository.findAll();
	}
	
	public Mono<Answer> saveAnswer(Answer answer,Flux<Quiz> quizFlux){
		if(quizFlux.hasElements().block()) {
			for (Quiz element : quizFlux.toIterable()) {
				Answer newAnswer = new Answer();
				
				newAnswer.setQuizid(element.getId());
				newAnswer.setProductid(element.getProduct());
				newAnswer.setLessionid(element.getLessionid());
				newAnswer.setAccountid(answer.getAccountid());
				newAnswer.setResult(0l);
				newAnswer.setQuestion(element.getQuestion());
				
				newAnswer.setChoicefour(element.getChoicefour());
				newAnswer.setChoiceone(element.getChoiceone());
				newAnswer.setChoicethree(element.getChoicethree());
				newAnswer.setChoicetwo(element.getChoicetwo());
				
				newAnswer.setCorrectAnswer(element.getCorrectAnswer());
				try {
					Mono<Answer> newAnswer1 = answerRepository.save(newAnswer);
					log.info( newAnswer1.block().toString());
				} catch (Exception e) {
					log.info( e.toString());
				}

			}	
			return Mono.just(answer);
		}
		return Mono.just(null);
	}
	public Flux<Answer> getAnswerByAccount(Long accountid , Long Lessionid){
		return answerRepository.findbyLessionAccountid(accountid, Lessionid);
	}
	
	public Flux<Answer> editAnswer(Long id , List<Answer> answer){
		//0 là chưa trả lời
		//1 là true
		//2 là false
//	    private String id;
//	    private Long accountid;
//	    private Long quizid;
//	    private String answer;
//	    private Long result;
//	    private Long lessionid;
//	    private Long productid;

		Flux<Answer> answersBylession = answerRepository.findByLessionid(id);
		for(Answer answer2 : answer ) {
			Answer newanAnswer= answerRepository.findById(answer2.getId()).block();
			
		}
		return answerRepository.saveAll(answer);
	}
	public Flux<Answer> getAnswerById(Long lessionid){
		return answerRepository.findByLessionid(lessionid);
	}
}
