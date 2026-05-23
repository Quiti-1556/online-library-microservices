package com.library.reviewservice.service.impl;

import com.library.reviewservice.dto.ReviewRequestDTO;
import com.library.reviewservice.dto.ReviewResponseDTO;
import com.library.reviewservice.entity.Review;
import com.library.reviewservice.repository.ReviewRepository;
import com.library.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        logger.info("Creando vistas");
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
