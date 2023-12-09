package com.example.demo.Repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.example.demo.Entity.AccountEntity;

import reactor.core.publisher.Mono;


public interface AccountRepository extends R2dbcRepository<AccountEntity, Long>{
	Mono<AccountEntity> findByEmail(String email);
}

