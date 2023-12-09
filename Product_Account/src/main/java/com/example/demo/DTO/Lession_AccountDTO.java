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
	private long id;
	private long lessionId;
	private long accountId;
	private long score;
	private long time;
}
