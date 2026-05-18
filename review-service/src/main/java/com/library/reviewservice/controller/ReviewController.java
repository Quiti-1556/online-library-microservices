package com.library.reviewservice.controller;

import com.library.reviewservice.dto.ReviewRequestDTO;
import com.library.reviewservice.dto.ReviewResponseDTO;
import com.library.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ReviewResponseDTO createReview(@RequestBody ReviewRequestDTO request) {

        return reviewService.createReview(request);
    }
}
