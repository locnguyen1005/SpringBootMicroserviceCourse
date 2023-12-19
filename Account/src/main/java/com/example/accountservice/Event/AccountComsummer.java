package com.example.accountservice.Event;



import com.example.commonservice.utils.ConstantCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountComsummer {
	@Autowired
	Gson gson;
	@KafkaListener(id = "PaymentSendAccount" , topics = ConstantCommon.ACCOUNT)
    public void commsumerACCOUNT(String product) {
		com.example.commonservice.Model.Product productEntity = gson.fromJson(product, com.example.commonservice.Model.Product.class);
    };
}
