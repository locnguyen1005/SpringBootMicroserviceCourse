package com.example.lessionservice.DTO;

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
public class LessionDTO {
	private Long id;
	private String video;

	private String title;
	private String description;
	private Long productId;

	private Long deletesoft;
	private String date;

}