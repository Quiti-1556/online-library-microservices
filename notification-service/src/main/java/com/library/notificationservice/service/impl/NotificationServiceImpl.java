package com.library.notificationservice.service.impl;

import com.library.notificationservice.dto.NotificationRequestDTO;
import com.library.notificationservice.dto.NotificationResponseDTO;
import com.library.notificationservice.entity.Notification;
import com.library.notificationservice.repository.NotificationRepository;
import com.library.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger =
            LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO request) {
        logger.info("Creando una notificacion");
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
