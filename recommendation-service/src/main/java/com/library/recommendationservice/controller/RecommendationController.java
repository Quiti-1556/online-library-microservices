package com.library.recommendationservice.controller;

import com.library.recommendationservice.dto.RecommendationRequestDTO;
import com.library.recommendationservice.dto.RecommendationResponseDTO;
import com.library.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping
    public RecommendationResponseDTO createRecommendation(@RequestBody RecommendationRequestDTO request) {

        return recommendationService.createRecommendation(request);
    }
}
