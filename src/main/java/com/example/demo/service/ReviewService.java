package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.util.MessageLocalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MessageLocalizer localizer;

    public List<Review> findByProduct(int productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review create(Review review, int productId) {
        Optional<Product> product = productRepository.findById(productId);
        review.setProduct(product.orElse(null));

        return reviewRepository.prepareAndSave(review);
    }

    public Optional<Review> find(Integer reviewId, Integer productId) {
        return reviewRepository.findByIdAndProductId(reviewId, productId);
    }

    public void update(Review review, Integer id, Integer productId) {
        reviewRepository
                .findByIdAndProductId(id, productId)
                .ifPresent(reviewUpdated -> {
                    reviewUpdated.setText(review.getText());
                    reviewRepository.save(reviewUpdated);
                });
    }

    public void delete(Integer id, Integer productId) {
        reviewRepository.findByIdAndProductId(id, productId)
                .orElseThrow(() -> new NotFoundException(localizer.getMessage("product.not.found", id)));
        reviewRepository.deleteById(id);
    }
}
