package com.example.emailservice.EmailController;

import com.example.emailservice.EmailService.EmailService;
import com.example.emailservice.Entity.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;

@RestController
public class EmailController {
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/mail")
	public String demo() throws MessagingException {
		Mail mailsend = new Mail();
		mailsend.setRecipient("locchotvt10057@gmail.com");
		mailsend.setMsgBody("<html><body><h1>Bạn đã đặt hàng thành công</h1></body></html>" 
							+"<p></strong>Tên khóa học của bạn là</strong>: Khóa học 1</p>"
							+"<p><strong>Giá tiền</strong>: 10.000</p>"
							+"<img src=\"https://mailtrap.io/wp-content/uploads/2022/12/test-email-picture.png\" id=\"https://mailtrap.io/wp-content/uploads/2022/12/test-email-picture.png\" alt=\"https://mailtrap.io/wp-content/uploads/2022/12/test-email-picture.png\">");
							
		
		mailsend.setSubject("Đăng ký khóa học thành công");
		String status = emailService.sendSimpleMail(mailsend);
		return status;
	}
}
