package com.example.demo.Model;

import java.util.List;

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
	private long id;
	@Column("lessionid")
	private long lessionId;
	@Column("accountid")
	private long accountId;
	private long score;

}
