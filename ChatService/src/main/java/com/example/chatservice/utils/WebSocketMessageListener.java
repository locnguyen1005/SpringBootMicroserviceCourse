package com.example.chatservice.utils;

import org.springframework.stereotype.Component;

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
