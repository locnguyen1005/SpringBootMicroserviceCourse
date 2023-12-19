package com.example.answerservice.Controller;

import java.util.List;

import com.example.answerservice.Service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.answerservice.Model.Answer;
import com.example.commonservice.Model.Quiz;
import com.example.answerservice.Repository.AnswerRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/Answer")
@CrossOrigin(origins = "http://localhost:3006")
@Slf4j
public class AnswerConrtoller {
	@Autowired
	private AnswerService answerService;

	@Autowired
	private WebClient.Builder webBuilder;
	@Autowired
	private AnswerRepository answerRepository;
	@GetMapping("/getall")
	public Flux<Answer> getAllAnswer(){
		return answerService.getAllAnswer();
	}
	@PostMapping("/Create")
    public Mono<Answer> createQuiz(@RequestBody Answer answer) {
		log.info(answer.toString());
		Flux<Quiz> resultQuizInProduct = webBuilder.build().get()
                .uri("http://localhost:9001/Quiz/GetAll/"+answer.getProductid().toString())
                .retrieve()
                .bodyToFlux(Quiz.class);
		
		if(resultQuizInProduct.hasElements().block()) {
			log.info("loc");
			return answerService.saveAnswer(answer , resultQuizInProduct);
		}
		else {
			log.info("loc1");
			return Mono.just(answer);
		}
        
    }
	@PutMapping("/Edit/{id}")//trỏ lessionid vô
    public Flux<Answer> submit(@PathVariable Long id ,@RequestBody List<Answer> answer) {
        return answerService.editAnswer(id,answer);
    }

	@GetMapping("/Loc")
    public Mono<Answer> demo(@RequestBody Answer answer) {
		log.info("answer true");
		return answerRepository.save(answer);
    }
	@PutMapping("/find/{lessionid}")//trỏ lessionid vô
    public Flux<Answer> submit(@PathVariable Long lessionid ) {
        return answerService.getAnswerById(lessionid);
    }
	@GetMapping("/GetLession/{accountid}/{lessionid}")
	public Flux<Answer> getAllAnswerByAccount(@PathVariable Long accountid, @PathVariable Long lessionid){
		return answerService.getAnswerByAccount(accountid, lessionid);
	}
}
