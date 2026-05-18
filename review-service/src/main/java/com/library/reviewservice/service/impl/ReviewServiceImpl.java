package com.library.reviewservice.service.impl;

import com.library.reviewservice.dto.ReviewRequestDTO;
import com.library.reviewservice.dto.ReviewResponseDTO;
import com.library.reviewservice.entity.Review;
import com.library.reviewservice.repository.ReviewRepository;
import com.library.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {

        Review review = new Review();

        review.setUserId(request.getUserId());
        review.setBookId(request.getBookId());
        review.setComment(request.getComment());
        review.setRating(request.getRating());

        Review savedReview = reviewRepository.save(review);

        ReviewResponseDTO response = new ReviewResponseDTO();

        response.setId(savedReview.getId());
        response.setUserId(savedReview.getUserId());
        response.setBookId(savedReview.getBookId());
        response.setComment(savedReview.getComment());
        response.setRating(savedReview.getRating());

        return response;
    }
}
