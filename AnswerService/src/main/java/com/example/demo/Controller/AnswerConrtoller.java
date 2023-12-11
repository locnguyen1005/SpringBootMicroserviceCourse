package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Model.Answer;
import com.example.demo.Model.Quiz;
import com.example.demo.Repository.AnswerRepository;
import com.example.demo.Service.AnswerService;

import jakarta.ws.rs.PUT;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/Answer")
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
}
