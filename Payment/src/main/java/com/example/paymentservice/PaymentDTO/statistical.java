package com.example.paymentservice.PaymentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class statistical {
    private Long id;
    private Long accountid;
    private Long productid;
    private Long price;
    private String productstreamid;
    private String nameproduct;
}
