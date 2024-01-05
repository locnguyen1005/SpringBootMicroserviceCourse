package com.example.LessionStreamService.LessionStreamEntity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Document(collection = "LessionStream")
public class LessionStream {
    @Id
    private String id;
    private Long accountid;
    private String title;
    private String description;
    private String productId;
    private String date;
    private String videoapi;
    private String secretkey;
}

