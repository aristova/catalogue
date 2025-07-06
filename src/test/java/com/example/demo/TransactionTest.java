package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.example.demo.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransactionTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void testPropagationBehaviour() {
        assertThrows(RuntimeException.class, () -> {
            productService.updateProductWithReviews(getUpdated(), getReviewUpdated());
        });

        Optional<Product> product = productRepository.findById(PRODUCT_1_ID);
        assertThat(product)
                .isPresent()
                .get()
                .extracting(Product::getTitle)
                .isNotEqualTo(getUpdated().getTitle());

        // ProductService REQUIRED, NESTED, SUPPORTS, NEVER, MANDATORY
        Optional<Review> review = reviewRepository.findById(REVIEW_1_ID);
        assertThat(review)
                .isPresent()
                .get()
                .extracting(Review::getText)
                .isNotEqualTo(getReviewUpdated().getText());

        // ProductService REQUIRED_NEW, NOT_SUPPORTED
//        Optional<Review> review = reviewRepository.findById(REVIEW_1_ID);
//        assertThat(review)
//                .isPresent()
//                .get()
//                .extracting(Review::getText)
//                .isEqualTo(getReviewUpdated().getText());


    }
}
