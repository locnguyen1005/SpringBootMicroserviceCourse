package com.example.demo.Repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.Model.AccountRegister;
import com.example.demo.Model.Account_Product;

import reactor.core.publisher.Flux;

public interface Product_Account_Reopository extends R2dbcRepository<AccountRegister,Long> {
    @Query("SELECT * FROM accountregister WHERE accountid = :accountId")
	Flux<AccountRegister> findByAccountId(long accountId);
    
    @Query("SELECT * FROM accountregister WHERE productId = :productId")
	Flux<AccountRegister> findByProductId(long productId);
}
