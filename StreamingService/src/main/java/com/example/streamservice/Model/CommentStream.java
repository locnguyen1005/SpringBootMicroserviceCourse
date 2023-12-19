package com.example.streamservice.Model;

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
public class CommentStream {
	private String comment;
	private String accountid;
	private String streamid;
}
