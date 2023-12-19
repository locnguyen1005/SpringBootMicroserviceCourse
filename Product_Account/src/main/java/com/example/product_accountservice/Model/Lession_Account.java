package com.example.product_accountservice.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "lessionaccount")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lession_Account {
	@Id
	private Long id;
	@Column("lessionid")
	private long lessionId;
	@Column("accountid")
	private Long accountId;
	private Long score;
	private String time;
	private Long success;
	private Long productid;
	
}
