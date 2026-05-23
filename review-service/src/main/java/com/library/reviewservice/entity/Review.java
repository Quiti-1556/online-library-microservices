package com.library.reviewservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long bookId;

    private String comment;

    private Integer rating;

}
