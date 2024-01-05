package com.example.commonservice.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStream {
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
