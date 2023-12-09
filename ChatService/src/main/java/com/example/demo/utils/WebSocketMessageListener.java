package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.demo.Model.ChatMessage;

import com.example.demo.Model.Status;
import com.google.common.base.Objects;

@Component
public class WebSocketMessageListener {

//    private final SimpMessageSendingOperations messagingTemplate;
//
//    @Autowired
//    public WebSocketMessageListener(SimpMessageSendingOperations messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
//
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//
//        if (username != null) {
//            ChatMessage chatMessage = ChatMessage.builder()
//                    .messageAction(Status.LEAVE)
//                    .chatUser(username)
//                    .build();
//
//            messagingTemplate.convertAndSend("/topic/chat", chatMessage);
//        }
//    }
}
