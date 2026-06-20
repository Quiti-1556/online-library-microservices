package com.library.recommendationservice.controller;

import com.library.recommendationservice.dto.RecommendationRequestDTO;
import com.library.recommendationservice.dto.RecommendationResponseDTO;
import com.library.recommendationservice.service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private static final Logger log = LoggerFactory.getLogger(RecommendationController.class);
    private final RecommendationService recommendationService;

    @GetMapping("/health")
    public String health() {
        return "Recommendation Service funcionando correctamente";
    }

    @PostMapping
    public ResponseEntity<RecommendationResponseDTO> createRecommendation(@Valid @RequestBody RecommendationRequestDTO request) {
        log.info("Creando recomendación para userId {}", request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(recommendationService.createRecommendation(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponseDTO>> getByUser(@PathVariable Long userId) {
        log.info("Consultando recomendaciones del usuario {}", userId);
        return ResponseEntity.ok(recommendationService.getRecommendationsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationResponseDTO> getById(@PathVariable Long id) {
        log.info("Buscando recomendación con id {}", id);
        return ResponseEntity.ok(recommendationService.getRecommendationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        log.info("Eliminando recomendación con id {}", id);
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.noContent().build();
    }
}
