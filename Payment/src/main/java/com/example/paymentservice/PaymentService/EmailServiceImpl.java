package com.example.paymentservice.PaymentService;

import com.example.paymentservice.PaymentDTO.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
	@Autowired 
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}") 
	private String sender;

	@Override
	public String sendSimpleMail(Mail details) {
		log.info(details.toString());
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		// Setting up necessary details
		mailMessage.setFrom(sender);
		mailMessage.setTo(details.getRecipient());
		mailMessage.setText(details.getMsgBody());
		mailMessage.setSubject(details.getSubject());
		// Sending the mail
		javaMailSender.send(mailMessage);
		return sender;

	}

}