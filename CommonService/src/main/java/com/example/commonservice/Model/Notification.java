package com.example.commonservice.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String id;
    private String notification;
    private Long accountid;
    private String image;
    private Long readcheck;
}
