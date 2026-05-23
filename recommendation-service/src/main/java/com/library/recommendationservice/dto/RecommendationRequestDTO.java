package com.library.recommendationservice.dto;

import lombok.Data;

@Data
public class RecommendationRequestDTO {
    private Long userId;

    private Long bookId;

    private String recommendationText;
}
