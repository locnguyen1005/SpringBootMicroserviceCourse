package com.example.demo.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.Model.ChatMessage;
import com.example.demo.Model.ChatMessageDTO;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.utils.ChatMessageBinder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ChatMessageService {

    private final ChatMessageBinder chatMessageBinder;

    private final ChatRepository chatMessageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public ChatMessageService(ChatMessageBinder chatMessageBinder, ChatRepository chatMessageRepository) {
        this.chatMessageBinder = chatMessageBinder;
        this.chatMessageRepository = chatMessageRepository;
    }
    public Flux<ChatMessage> getallchatmessage(){
    	return chatMessageRepository.findAll();
    }
    public Mono<ChatMessage> saveChatMessageToDB(ChatMessageDTO chatMessageDTO) {
    	if(chatMessageDTO.getMessage() != null) {
    		chatMessageDTO.setDate(LocalDateTime.now());
        	ChatMessage chatMessage = modelMapper.map(chatMessageDTO, ChatMessage.class);
            
            log.info(chatMessage.toString());
            return chatMessageRepository.save(chatMessage);
    	}
    	else {
    		return Mono.error(new Exception("Message id null"));
    	}
    }
    public Flux<ChatMessage> getallchatmessagebyproductid(Long productid){
    	return chatMessageRepository.findByProductid(productid);
    }
}