package com.example.commonservice.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	private Long id;
	private String name;
	private Long accountid;
	private Long price;
	private String description;
	private String folder;
	private String category;
	private String image;
	private String apiimage;
}
