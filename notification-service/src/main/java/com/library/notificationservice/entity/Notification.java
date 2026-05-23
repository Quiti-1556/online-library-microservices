package com.library.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String message;

    private String type;
}
