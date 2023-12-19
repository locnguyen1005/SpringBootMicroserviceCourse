package com.example.commonservice.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
	private long id;
	private long lessionid;
	private long product;
	private String question;
	private String choiceone;
	private String choicetwo;
	private String choicethree;
	private String choicefour;
	private String correctAnswer;
}
