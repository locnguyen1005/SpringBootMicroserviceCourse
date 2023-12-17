package com.example.demo.Controller;

import java.util.List;

import org.apache.coyote.http11.filters.VoidInputFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Model.Answer;
import com.example.demo.Model.Comment;
import com.example.demo.Model.Quiz;

import com.example.demo.Service.CommentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {
	@Autowired
	private CommentService commentService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private WebClient.Builder webBuilder;

	@GetMapping("/getall")
	public Flux<CommentDTO> getAllAnswer(){
		return commentService.getAllAnswer();
	}
	@GetMapping("/getall/{productid}")
	public Flux<Comment> getAllAnswer(@PathVariable Long productid){
		return commentService.getAllAnswerbyid(productid);
	}
	@PostMapping("/Create")
    public Mono<Comment> createQuiz(@RequestParam("data") Comment message , @RequestParam(value = "file") MultipartFile file) {
		
        return commentService.saveAnswer(message, file);
    }
    @MessageMapping("/sendNotification/{productid}")
    @SendTo("/topic/notification/loc/{productid}")
    public Comment  recMessage1(@RequestParam("data") Comment message , @RequestParam(value = "file") MultipartFile file){
        return commentService.saveAnswer(message , file).block();
    }
    @PutMapping("/edit/{accountid}/{commentid}")
    public void editComment(@PathVariable Long accountid , @PathVariable String commentid , @RequestParam("data") Comment message , @RequestParam(value = "file") MultipartFile file) {
    	
    }
//	@PutMapping("/Edit/{id}")
//    public Flux<Answer> submit(@PathVariable Long id ,@RequestBody List<Answer> answer) {
//        return commentService.editAnswer(id,answer);
//    }

}
