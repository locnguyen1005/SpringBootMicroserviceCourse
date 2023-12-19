package com.example.emailservice.Event;

import com.example.emailservice.Entity.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.emailservice.EmailService.EmailService;
import com.example.commonservice.utils.ConstantCommon;
import com.google.gson.Gson;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailComsummer {
	@Autowired
	private EmailService emailService;
	@Autowired
	Gson gson = new Gson();
	@KafkaListener(id = "PaymentSendEmail" , topics = ConstantCommon.EMAIL)
    public void commsumerEmail(String mail) throws MessagingException{
		Mail mailSend = gson.fromJson(mail, Mail.class);
		log.info(mail);
		String status = emailService.sendSimpleMail(mailSend);		
    };
 
}
