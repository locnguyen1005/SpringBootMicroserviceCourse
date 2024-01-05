package com.example.product_accountservice.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account_ProductStream {
    private String productid;
    private Long accountid;
    private String nameproduct;
    private String description;
    private String category;
    private String image;
    private String email;
    private Long price;
    private String avaterimage;
    private String fullname;
}
