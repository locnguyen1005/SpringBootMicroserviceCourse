package com.example.LessionStreamService.LessionStreamDTO;

import lombok.*;

@Data
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LessionStreamDTO {
    private String id;
    private Long accountid;
    private String title;
    private String description;
    private String productId;
    private String date;
    private String videoapi;
    private String secretkey;
}
