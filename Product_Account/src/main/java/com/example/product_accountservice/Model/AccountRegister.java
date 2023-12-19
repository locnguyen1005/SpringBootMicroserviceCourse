package com.example.product_accountservice.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "accountregister")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegister {
	@Id
	private long id;
	@Column("ProductId")
	private long productId;
	@Column("AccountID")
	private long accountId;
	
}
