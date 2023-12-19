package com.example.productservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductClient {
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
