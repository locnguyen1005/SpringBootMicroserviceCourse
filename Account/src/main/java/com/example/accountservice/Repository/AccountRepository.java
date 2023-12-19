package com.example.accountservice.Repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.example.accountservice.Entity.AccountEntity;

import reactor.core.publisher.Mono;


public interface AccountRepository extends R2dbcRepository<AccountEntity, Long>{
	Mono<AccountEntity> findByEmail(String email);
}

