package com.example.productstream.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Document(collection = "productStream")
public class ProductStream {
    @Id
    private String id;
    private String name;
    private Long accountid;
    private Long price;
    private String description;
    private List<Integer> date;
    private String time;
    private String dateStart;
    private String category;
    private String image;
    private Long type;
}
