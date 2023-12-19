package com.example.commonservice.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lession {
	private Long id;
	private String video;
	private String path;
	private String title;
	private String description;
	private Long productId;
	private String folder;
	private Long deletesoft;
	private String date;
}
