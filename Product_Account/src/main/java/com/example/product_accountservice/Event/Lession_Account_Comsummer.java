package com.example.product_accountservice.Event;

import com.example.product_accountservice.Repository.Lession_Account_Repository;
import com.example.product_accountservice.Repository.Product_Account_Reopository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.product_accountservice.Model.AccountRegister;
import com.example.commonservice.Model.Lession;
import com.example.product_accountservice.Model.Lession_Account;
import com.example.commonservice.utils.ConstantCommon;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class Lession_Account_Comsummer {
	@Autowired
	private Lession_Account_Repository lession_Account_Repository;
	@Autowired
	private Product_Account_Reopository product_Account_Reopository;
	@Autowired
	Gson gson = new Gson();
	@Autowired
	private WebClient.Builder webBuilder;

	@KafkaListener(id = "AccountSendCreate", topics = ConstantCommon.LESSION_ACCOUNT)
	public void commsumerLession_Account(String mail) {
		Lession lession = gson.fromJson(mail, Lession.class);
		int i=0;

		Lession_Account lession_Account = gson.fromJson(mail, Lession_Account.class);
//		Flux<AccountRegister> accountregisterFlux = product_Account_Reopository.findByProductId(lession.getProductId());
//		if(accountregisterFlux.hasElements().block()) {
//			for (AccountRegister element : accountregisterFlux.toIterable()) {
//				Lession_Account newLession_Account = new Lession_Account();
//				newLession_Account.setAccountId(element.getAccountId());
//				newLession_Account.setLessionId(lession.getId());
//				newLession_Account.setProductid(lession.getProductId());
//				newLession_Account.setScore(0l);
//				newLession_Account.setSuccess(0l);
//				newLession_Account.setTime(null);
//			}	
//		}
//		else {
//			
//		}
		// kiểm tra có người đăng ký chưa

		if (lession.getId() != null) {
			Flux<AccountRegister> accountregisterFlux = product_Account_Reopository
					.findByProductId(lession.getProductId());
			if (accountregisterFlux.hasElements().block()) {
				for (AccountRegister element : accountregisterFlux.toIterable()) {
					
					log.info(element.toString());
					Lession_Account newLession_Account = new Lession_Account();
					newLession_Account.setAccountId(element.getAccountId());
					newLession_Account.setLessionId(lession.getId());
					newLession_Account.setProductid(lession.getProductId());
					newLession_Account.setScore(0l);
					newLession_Account.setSuccess(0l);
					newLession_Account.setTime(null);
					Mono<Lession_Account> newMonoLession_Account = lession_Account_Repository.save(newLession_Account);
					log.info(newMonoLession_Account.block().toString());
				}
			}
		} else {

			Flux<Lession> resultProduct = webBuilder.build().get()
					.uri("http://localhost:9003/Lession/GetAllBy/" + lession_Account.getProductid()).retrieve()
					.bodyToFlux(Lession.class);
			for (Lession element : resultProduct.toIterable()) {
				if(i==0) {
					i++;
					log.info(element.toString());
					Lession_Account newLession_Account = new Lession_Account();
					newLession_Account.setAccountId(lession_Account.getAccountId());
					newLession_Account.setLessionId(element.getId());
					newLession_Account.setProductid(element.getProductId());
					newLession_Account.setScore(0l);
					newLession_Account.setSuccess(0l);
					newLession_Account.setTime(null);
					Mono<Lession_Account> newMonoLession_Account = lession_Account_Repository.save(newLession_Account);
					log.info(newMonoLession_Account.block().toString());
					
				}
				else {
					log.info(element.toString());
					Lession_Account newLession_Account = new Lession_Account();
					newLession_Account.setAccountId(lession_Account.getAccountId());
					newLession_Account.setLessionId(element.getId());
					newLession_Account.setProductid(element.getProductId());
					newLession_Account.setScore(0l);
					newLession_Account.setSuccess(0l);
					newLession_Account.setTime(null);
					Mono<Lession_Account> newMonoLession_Account = lession_Account_Repository.save(newLession_Account);
					log.info(newMonoLession_Account.block().toString());
				}
				
			}

		}
	};
}
