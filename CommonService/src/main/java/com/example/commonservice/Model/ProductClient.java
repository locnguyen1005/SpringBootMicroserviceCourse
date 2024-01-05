package com.example.commonservice.Model;

import lombok.*;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductClient {
    private Long id;
    private String name;
    private Long accountid;
    private Long price;
    private String description;
    private String folder;
    private String category;
    private String image;
    private String apiimage;
}
