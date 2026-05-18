package com.library.reviewservice.service;


import com.library.reviewservice.dto.ReviewRequestDTO;
import com.library.reviewservice.dto.ReviewResponseDTO;

public interface ReviewService {
    ReviewResponseDTO createReview(ReviewRequestDTO request);
}
