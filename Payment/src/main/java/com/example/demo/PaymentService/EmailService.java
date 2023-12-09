package com.example.demo.PaymentService;

import com.example.demo.PaymentDTO.Mail;

public interface EmailService {
	String sendSimpleMail(Mail details);

}
