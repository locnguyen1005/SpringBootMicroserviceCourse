package com.example.lessionservice.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Table(name = "lession")
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessionEntity {
	@Id
	private Long id;
	private String video;

	private String title;
	private String description;
	@Column("productId")
	private Long productId;

	private Long deletesoft;
	private String date;
}
