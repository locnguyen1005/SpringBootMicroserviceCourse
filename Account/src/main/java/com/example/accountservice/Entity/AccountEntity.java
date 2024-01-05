package com.example.accountservice.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Table("account")
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {
	@Id
	@Column("id")
	private Long id;
	private String password;
	private String email;
	private String fullname;
	private String avaterimage;
	private String role;
	private String phonenumber;
	private String address;
	private Long balance;
	
}