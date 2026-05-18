package com.library.recommendationservice.repository;

import com.library.recommendationservice.dto.RecommendationResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<RecommendationResponseDTO,Long> {
}
