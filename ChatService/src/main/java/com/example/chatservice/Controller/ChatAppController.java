package com.example.chatservice.Controller;


import com.example.chatservice.Service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.chatservice.Model.ChatMessage;
import com.example.chatservice.Model.ChatMessageDTO;

import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;


@Controller
@CrossOrigin(origins = "http://localhost:3006")
@RequestMapping("Chat")
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
