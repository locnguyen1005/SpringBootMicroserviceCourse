package com.example.productstream.ProductStreamDTO;

import lombok.*;

import java.util.List;

@Data
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ProductStreamDTO {
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
    private String enddate;
    private Long type;
}
