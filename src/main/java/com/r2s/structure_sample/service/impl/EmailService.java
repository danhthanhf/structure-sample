package com.r2s.structure_sample.service.impl;

import com.r2s.structure_sample.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements NotificationService {

    @Override
    public void sendNotification(String t, String message) {
        System.out.println("Sending email to: " + t + " with message: " + message);
    }
}
