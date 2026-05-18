package com.library.notificationservice.service.impl;

import com.library.notificationservice.dto.NotificationRequestDTO;
import com.library.notificationservice.dto.NotificationResponseDTO;
import com.library.notificationservice.entity.Notification;
import com.library.notificationservice.repository.NotificationRepository;
import com.library.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO request) {

        Notification notification = new Notification();

        notification.setUserId(request.getUserId());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());

        Notification savedNotification = notificationRepository.save(notification);

        NotificationResponseDTO response = new NotificationResponseDTO();

        response.setId(savedNotification.getId());
        response.setUserId(savedNotification.getUserId());
        response.setMessage(savedNotification.getMessage());
        response.setType(savedNotification.getType());

        return response;
    }
}
