package com.example.streamservice.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "Streaming")
public class Stream {
	@Id
    private String id;
    private Long accountid;
	private String title;
	private String description;
	private String productId;
	private String date;
	private String videoapi;
	private String secretkey;
}
