package com.example.demo.DTO;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import com.example.demo.Model.Lession_Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lession_AccountDTO {
	private Long id;
	private Long lessionId;
	private Long accountId;
	private Long score;
	private Long time;
	private Long success;
	private Long productid;
}
