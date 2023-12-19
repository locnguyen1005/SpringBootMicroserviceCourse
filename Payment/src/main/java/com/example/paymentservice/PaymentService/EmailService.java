package com.example.paymentservice.PaymentService;

import com.example.paymentservice.PaymentDTO.Mail;

public interface EmailService {
	String sendSimpleMail(Mail details);

}
