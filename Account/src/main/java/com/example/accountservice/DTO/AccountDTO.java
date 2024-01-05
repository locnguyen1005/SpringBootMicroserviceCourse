package com.example.accountservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
	private Long id;
	private String password;
	private String email;
	private String fullname;
	private String avaterimage;
	private String role;
	private String phonenumber;
	private String address;
	private long balance;
}
