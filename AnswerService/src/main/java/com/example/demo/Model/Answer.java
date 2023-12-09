package com.example.demo.Model;

import java.time.LocalDateTime;

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
@Document(collection = "Answer")
public class Answer {
    @Id
    private String id;
    private Long accountid;
    private Long quizid;
    private String answer;
    private Long result;
    private Long lessionid;
    private Long productid;
}
