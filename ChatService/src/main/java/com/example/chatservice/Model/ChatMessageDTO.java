package com.example.chatservice.Model;

import java.time.LocalDateTime;


import lombok.*;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageDTO {
	    private String id;
		private String senderName;
	    private String receiverName;
	    private Long productid;
	    private Long accountid;
	    private String message;
	    private LocalDateTime date;
	    private Status status;
}
