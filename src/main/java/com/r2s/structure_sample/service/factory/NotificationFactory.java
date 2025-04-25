package com.r2s.structure_sample.service.factory;

import com.r2s.structure_sample.common.enums.SendNotificationType;
import com.r2s.structure_sample.service.NotificationService;
import com.r2s.structure_sample.service.impl.EmailService;
import com.r2s.structure_sample.service.impl.SmsService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationFactory {
    private final Map<SendNotificationType, NotificationService> serviceMap;

    public NotificationFactory(List<NotificationService> services) {
        serviceMap = new HashMap<>();
        for (NotificationService service : services) {
            if (service instanceof EmailService) {
                serviceMap.put(SendNotificationType.EMAIL, service);
            } else if (service instanceof SmsService) {
                serviceMap.put(SendNotificationType.SMS, service);
            }
        }
    }

    public NotificationService getService(SendNotificationType type) {
        return serviceMap.get(type);
    }
}
