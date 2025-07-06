package com.example.demo.to;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import lombok.Data;

@Data
public class ProductWithReviews {
    private Product product;
    private Review review;
}
