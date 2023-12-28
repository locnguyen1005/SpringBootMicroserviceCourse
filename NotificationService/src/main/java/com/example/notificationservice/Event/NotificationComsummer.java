package com.example.notificationservice.Event;

import com.example.notificationservice.Model.Notification;
import com.example.notificationservice.NotificationService.NotificationService;
import com.google.gson.Gson;
import org.aspectj.util.GenericSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.commonservice.utils.ConstantCommon;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationComsummer {
	@Autowired
	Gson gson;
	private NotificationService notificationService;
	@KafkaListener(id = "Notification" , topics = ConstantCommon.NOTIFICATION)
    public void commsumerNotification(String notification) {
		notificationService.saveAnswer(gson.fromJson(notification, Notification.class));
    };
}
