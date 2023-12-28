package com.example.accountservice.Event;



import com.example.accountservice.DTO.AccountDTO;
import com.example.accountservice.Entity.AccountEntity;
import com.example.accountservice.Service.AccountService;
import com.example.commonservice.utils.ConstantCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountComsummer {
	@Autowired
	Gson gson;
	@Autowired
	AccountService accountService;
	@KafkaListener(id = "PaymentSendAccount" , topics = ConstantCommon.ACCOUNT)
    public void commsumerACCOUNT(String product) {
		com.example.commonservice.Model.Product productEntity = gson.fromJson(product, com.example.commonservice.Model.Product.class);
		Mono<AccountEntity> accountEntityMono = accountService.accountTeachTransferMoney(productEntity.getAccountid() , productEntity.getPrice());
    };
}
