package com.example.notificationservice.Model;

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
@Document(collection = "Notification")
public class Notification {
    @Id
    private String id;
    private String notification;
    private String accountid;
    private String image;
    private Long readcheck;
    private Long type;
}
