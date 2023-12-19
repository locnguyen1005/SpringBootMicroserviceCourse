package com.example.notificationservice.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.notificationservice.Model.Notification;
import com.example.notificationservice.Repository.NotificationRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;
	
	public Flux<Notification> getAllNotification(){
		return notificationRepository.findAll();
	}
	public Mono<Notification> saveAnswer(Notification notification){	
		return notificationRepository.save(notification);
	}
}
