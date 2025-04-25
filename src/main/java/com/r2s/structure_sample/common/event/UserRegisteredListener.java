package com.r2s.structure_sample.common.event;

import com.r2s.structure_sample.common.enums.SendNotificationType;
import com.r2s.structure_sample.entity.User;
import com.r2s.structure_sample.service.impl.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredListener {
    private final NotificationHandler notificationHandler;

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        User user = event.getUser();
        notificationHandler.notifyUser(user.getEmail(), "Welcome to our service!", SendNotificationType.EMAIL);
    }
}
