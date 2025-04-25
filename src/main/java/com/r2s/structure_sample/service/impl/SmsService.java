package com.r2s.structure_sample.service.impl;

import com.r2s.structure_sample.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class SmsService implements NotificationService {
    @Override
    public void sendNotification(String to, String message) {
        System.out.println("Send message to " + to + " with message: " + message);
    }
}
