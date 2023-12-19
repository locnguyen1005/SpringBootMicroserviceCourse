package com.example.notificationservice.Controller;

import com.example.notificationservice.NotificationService.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.notificationservice.Model.Notification;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/api/notification")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {
	@Autowired
	private NotificationService notificationService;
	@MessageMapping("/sendNotification")
    @SendTo("/topic/notifications")
    public Notification sendNotification(@Payload Notification notification) {
		
        return notificationService.saveAnswer(notification).block();
 
    }
	@GetMapping("/GetAll")
	public Flux<Notification> getAllAnswer(){
		return notificationService.getAllNotification();
	}
}
