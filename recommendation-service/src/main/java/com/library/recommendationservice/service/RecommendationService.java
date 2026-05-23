package com.library.recommendationservice.service;

import com.library.recommendationservice.dto.RecommendationRequestDTO;
import com.library.recommendationservice.dto.RecommendationResponseDTO;

public interface RecommendationService {
    RecommendationResponseDTO createRecommendation(RecommendationRequestDTO request);
}
