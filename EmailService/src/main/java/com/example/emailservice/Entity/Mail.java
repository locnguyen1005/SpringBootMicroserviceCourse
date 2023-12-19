package com.example.emailservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;

}