package com.library.notificationservice.dto;

import lombok.Data;

@Data
public class NotificationResponseDTO {
    private Long id;

    private Long userId;

    private String message;

    private String type;
}
