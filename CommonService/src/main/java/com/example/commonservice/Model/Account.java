package com.example.commonservice.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
	private Long id;
	private String password;
	private String email;
	private String fullname;
	private String avaterimage;
	private String role;
}
