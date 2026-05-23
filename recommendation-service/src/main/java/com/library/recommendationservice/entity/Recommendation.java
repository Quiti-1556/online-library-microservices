package com.library.recommendationservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Data
@Entity
@Table(name="recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long bookId;

    private String recommendationText;
}
