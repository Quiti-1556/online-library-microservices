package com.library.recommendationservice.dto;

import lombok.Data;

@Data
public class RecommendationResponseDTO {
    private Long id;

    private Long userId;

    private Long bookId;

    private String recommendationText;

}
