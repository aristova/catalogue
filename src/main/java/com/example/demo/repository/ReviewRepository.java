package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(int productId);

    default Review prepareAndSave(Review review) {
        return save(review);
    }

    Optional<Review> findByIdAndProductId(Integer reviewId, Integer productId);
}
