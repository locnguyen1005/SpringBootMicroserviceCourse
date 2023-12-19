package com.example.commonservice.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lession_Account {
	private Long id;
	private Long lessionId;
	private Long accountId;
	private Long score;
	private Long time;
	private Long success;
	private Long productid;
}

