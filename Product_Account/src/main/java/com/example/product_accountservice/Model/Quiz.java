package com.example.product_accountservice.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "quiz")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
	@Id
	private Long id;
	private Long lessionid;
	@Column("question")
	private String question;
	private String choiceone;
	private String choicetwo;
	private String choicethree;
	private String choicefour;
	@Column("correctAnswer")
	private String correctAnswer;
	@Column("productid")
	private Long productid;
}
