package com.example.chatservice.Service;

import java.time.LocalDateTime;

import com.example.chatservice.Repository.ChatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chatservice.Model.ChatMessage;
import com.example.chatservice.Model.ChatMessageDTO;
import com.example.chatservice.utils.ChatMessageBinder;

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