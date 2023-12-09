package com.example.demo.Model;

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
public class Answer {
    private String id;
    private Long accountid;
    private Long quizid;
    private String answer;
    private Long result;
    private Long lessionid;
    private Long productid;
}
