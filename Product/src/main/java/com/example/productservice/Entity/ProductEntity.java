package com.example.productservice.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {
	@Id
	private Long id;
	@Column("nameproduct")
	private String name;
	private Long accountid;
	private Long price;
	private String description;
	private String folder;
	private String category;
	private String image;
}
