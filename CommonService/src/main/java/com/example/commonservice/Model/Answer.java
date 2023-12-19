package com.example.commonservice.Model;

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
    private long accountid;
    private Long quizid;
    private String answer;
    private long result;
    private long lessionid;
    private long productid;
}
