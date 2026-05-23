package com.library.recommendationservice.service.impl;

import com.library.recommendationservice.dto.RecommendationRequestDTO;
import com.library.recommendationservice.dto.RecommendationResponseDTO;
import com.library.recommendationservice.entity.Recommendation;
import com.library.recommendationservice.repository.RecommendationRepository;
import com.library.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger logger =
            LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRepository recommendationRepository;

    @Override
    public RecommendationResponseDTO createRecommendation(RecommendationRequestDTO request) {
        logger.info("Creando recomendacion");
        Recommendation recommendation = new Recommendation();

        recommendation.setUserId(request.getUserId());
        recommendation.setBookId(request.getBookId());
        recommendation.setRecommendationText(request.getRecommendationText());

        Recommendation savedRecommendation = recommendationRepository.save(recommendation);

        RecommendationResponseDTO response = new RecommendationResponseDTO();

        response.setId(savedRecommendation.getId());
        response.setUserId(savedRecommendation.getUserId());
        response.setBookId(savedRecommendation.getBookId());
        response.setRecommendationText(savedRecommendation.getRecommendationText());

        return response;
    }
}
