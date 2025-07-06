package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.util.MessageLocalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

    private final MessageLocalizer localizer;

    private final ProductRepository productRepository;

    private final ReviewRepository reviewRepository;

    private final ReviewService reviewService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product create(Product product) {
        return productRepository.save(new Product(null, product.getTitle(), product.getDetails(), null, null));
    }

    public Optional<Product> find(Integer productId) {
        return productRepository.findById(productId);
    }

    public void update(Product product) {
        productRepository
                .findById(product.getId())
                .ifPresent(productUpdated -> {
                    productUpdated.setTitle(product.getTitle());
                    productUpdated.setDetails(product.getDetails());
                    productRepository.save(productUpdated);
                });
    }

    public void delete(Integer id) {
        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(localizer.getMessage("product.not.found", id)));
        productRepository.deleteById(id);
    }

    @Transactional
    public void updateProductWithReviews(Product product, Review review) {
        log.info("updateProductWithReviews TX IN >>>");
        update(product);
        reviewService.update(review, review.getId(), product.getId());
        log.info("<<< TX OUT updateProductWithReviews");
        throw new RuntimeException("Something went wrong in ProductService");
        //  throw new RuntimeException("Something went wrong in ProductService");
    }
}
