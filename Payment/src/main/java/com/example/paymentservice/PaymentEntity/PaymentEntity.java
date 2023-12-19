package com.example.paymentservice.PaymentEntity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
	@Id
	private Long id;
	@Column("accountid")
	private Long accountid;
	private Long productid;
	private LocalDateTime day;
	private Long price;

}
