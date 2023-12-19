package com.example.paymentservice.PaymentDTO;




import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private Long id;
	private Long accountid;
	private Long productid;
	private LocalDateTime day;
	private Long price;
	
}
