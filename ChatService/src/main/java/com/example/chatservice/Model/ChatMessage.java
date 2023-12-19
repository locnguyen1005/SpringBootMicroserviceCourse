package com.example.chatservice.Model;



import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Document(collection = "ChatSerivce")
public class ChatMessage {
    @Id
    private String id;
	private String senderName;
    private String receiverName;
    private Long productid;
    private Long accountid;
    private String message;
    private LocalDateTime date;
    private Status status;
}