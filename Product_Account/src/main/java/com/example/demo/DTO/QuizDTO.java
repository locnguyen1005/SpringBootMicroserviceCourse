package com.example.demo.DTO;

import org.springframework.data.relational.core.mapping.Column;

import com.example.demo.Model.Quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
	private Long id;
	private Long lessionid;
	private Long productid;
	private String question;
	private String choiceone;
	private String choicetwo;
	private String choicethree;
	private String choicefour;
	private String correctAnswer;
}
