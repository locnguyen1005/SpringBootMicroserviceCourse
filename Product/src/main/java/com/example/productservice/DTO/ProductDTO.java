package com.example.productservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Long id;
	private String name;
	private Long accountid;
	private Long price;
	private String description;
	private String folder;
	private Long category;
	private String image;
}
