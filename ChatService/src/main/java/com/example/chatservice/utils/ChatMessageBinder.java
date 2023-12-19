package com.example.chatservice.utils;

import org.springframework.stereotype.Component;


@Component
public class ChatMessageBinder {

//	public ChatMessage bind(ChatMessageDTO chatMessageDTO) {
//        Date messageTime = Calendar.getInstance().getTime();
//        setMessageTime(chatMessageDTO, messageTime);
//
//        return ChatMessage.builder()
//                .chatUser(chatMessageDTO.getChatUser())
//                .message(chatMessageDTO.getMessage())
//                .messageAction(Enum.valueOf(Status.class, chatMessageDTO.getStatus()))
//                .messageTime(messageTime)
//                .build();
//    }
//
//    private void setMessageTime(ChatMessageDTO chatMessageDTO, Date messageTime) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//        String formattedMessageTime = dateFormat.format(messageTime);
//        chatMessageDTO.setMessageTime(formattedMessageTime);
//    }
}
