package com.library.reviewservice.dto;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;

    private Long userId;

    private Long bookId;

    private String comment;

    private Integer rating;
}
