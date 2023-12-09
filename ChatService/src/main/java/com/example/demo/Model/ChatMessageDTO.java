package com.example.demo.Model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.data.annotation.Id;


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
