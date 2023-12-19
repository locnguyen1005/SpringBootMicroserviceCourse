package com.example.emailservice.EmailService;

import com.example.emailservice.Entity.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService  {
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}") 
	private String sender;

	public String sendSimpleMail(Mail details) throws MessagingException {
		log.info(details.toString());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		// Setting up necessary details
		mimeMessage.setFrom(sender);
		mimeMessage.setRecipients(MimeMessage.RecipientType.TO,details.getRecipient());
		mimeMessage.setText(details.getMsgBody());
		mimeMessage.setSubject(details.getSubject());
		mimeMessage.setContent(details.getMsgBody(), "text/html; charset=utf-8");
		// Sending the mail
		javaMailSender.send(mimeMessage);
		return sender;

	}

}