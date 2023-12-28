package com.example.streamservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class StreamDTO {
	private String id;
    private Long accountid;
	private String title;
	private String description;
	private String productId;
	private String date;
	private String videoapi;
	private String secretkey;
}
