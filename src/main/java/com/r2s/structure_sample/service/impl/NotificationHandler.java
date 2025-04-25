package com.r2s.structure_sample.service.impl;

import com.r2s.structure_sample.common.enums.SendNotificationType;
import com.r2s.structure_sample.service.NotificationService;
import com.r2s.structure_sample.service.factory.NotificationFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationHandler {

    private final NotificationFactory factory;

    public NotificationHandler(NotificationFactory factory) {
        this.factory = factory;
    }

    public void notifyUser(String to, String msg, SendNotificationType type) {
        NotificationService service = factory.getService(type);
        service.sendNotification(to, msg);
    }
}
