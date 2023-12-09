package com.example.demo.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.EndpointId;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Model.Answer;
import com.example.demo.Model.Quiz;
import com.example.demo.Repository.AnswerRepository;

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
		for (Quiz element : quizFlux.toIterable()) {
			Answer newAnswer = new Answer();
			
			newAnswer.setQuizid(element.getId());
			newAnswer.setProductid(element.getProduct());
			newAnswer.setLessionid(element.getLessionid());
			//0 là chưa trả lời
			//1 là true
			//2 là false
			newAnswer.setResult(0l);
			try {
				Mono<Answer> newAnswer1 = answerRepository.save(newAnswer);
				log.info( newAnswer1.block().toString());
			} catch (Exception e) {
				log.info( e.toString());
			}

		}	
		return Mono.just(answer);
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

}
