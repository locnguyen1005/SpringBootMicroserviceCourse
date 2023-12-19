package com.example.notificationservice.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.notificationservice.Model.Notification;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String>{

}
