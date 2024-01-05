package com.example.commentservice.Controller;

import com.example.commentservice.Model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.commentservice.Service.CommentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@CrossOrigin(origins = "http://localhost:3006")
@RequestMapping("Comment")
public class CommentController {
	@Autowired
	private CommentService commentService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private WebClient.Builder webBuilder;

	@GetMapping("/getall")
	public Flux<Comment> getAllAnswer(){
		return commentService.getAllAnswer();
	}
	@GetMapping("/getall/{productid}")
	public Flux<Comment> getAllAnswer(@PathVariable Long productid){
		return commentService.getAllAnswerbyid(productid);
	}
	@GetMapping("/getall/{lessionid}")
	public Flux<Comment> getAllAnswer1(@PathVariable Long lessionid){
		return commentService.getAlllesionid(lessionid);
	}
	@PostMapping("/Create")
    public Mono<Comment> createQuiz(@RequestBody Comment answer) {
		
        return commentService.saveAnswer(answer);
    }

    @MessageMapping("/sendNotification/{productid}")
    @SendTo("/topic/notification/loc/{productid}")
    public Comment  recMessage1(@Payload Comment message){
        return commentService.saveAnswer(message).block();
    }
//	@PutMapping("/Edit/{id}")
//    public Flux<Answer> submit(@PathVariable Long id ,@RequestBody List<Answer> answer) {
//        return commentService.editAnswer(id,answer);
//    }

}
