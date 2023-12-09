package com.example.demo.Model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account_Product {
	private Long productid;
	private Long accountid;
	private String nameproduct;
	private String description;
	private String category;
	private String image;
	private String email;
	private Long price;
	private String apiimage;
	private String avaterimage;
	private String fullname;
}
