package com.example.demo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.Model.ChatMessage;
import com.example.demo.Model.ChatMessageDTO;
import com.example.demo.Service.ChatMessageService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Controller
@CrossOrigin(origins = "http://localhost:3006")
public class ChatAppController {

    @Autowired
    private ChatMessageService service;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message/{productid}")
    @SendTo("/chatroom/public/{productid}")
    public ChatMessage chat(@Payload ChatMessageDTO chatMessage) {
        return service.saveChatMessageToDB(chatMessage).block();
    }

    @GetMapping("/getall/{productid}")
    public Flux<ChatMessage> getChatMessage(@PathVariable Long productid) {
    	return service.getallchatmessagebyproductid(productid);
    }
}
