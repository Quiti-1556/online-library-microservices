package com.library.reviewservice.repository;

import com.library.reviewservice.entity.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review,Long> {
}
