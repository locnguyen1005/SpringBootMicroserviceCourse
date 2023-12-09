package com.example.demo.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lession_Account {
	private long id;
	private long lessionId;
	private long accountId;
	private long score;
	private long time;
}

