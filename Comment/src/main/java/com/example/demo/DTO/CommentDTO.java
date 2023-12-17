package com.example.demo.DTO;

import com.example.demo.Model.Comment;

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
public class CommentDTO {
	private String id;
	private Long accountid;
	private Long productid;
	private String comment;
	private String image;
}
