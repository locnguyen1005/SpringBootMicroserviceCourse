package com.example.product_accountservice.DTO;

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
