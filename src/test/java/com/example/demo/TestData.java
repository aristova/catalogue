package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;

public class TestData {

    public static final int PRODUCT_1_ID = 1;
    public static final int PRODUCT_NOT_FOUND = 100;

    public static final int REVIEW_1_ID = 1;

    public static final int REVIEW_NOT_FOUND = 100;

    public static final String REVIEW_1_TEXT = "Really nice coffee mug";

    public static final String REVIEW_NEW_TEXT = "Great product!";

    public static final String PRODUCT_1_NAME = "Coffee Mug";

    public static Product getNew() {
        return new Product(null, "New product", "New product details", null, null);
    }

    public static Product getNewEmptyTitle() {
        return new Product(null, "", "Product details", null, null);
    }

    public static Product getNewShortDetails() {
        return new Product(null, "Some product", " ", null, null);
    }


    public static Product getUpdated() {
        return new Product(PRODUCT_1_ID, "Updated Product", "Updated Product details", null, null);
    }

    public static Review getReviewUpdated() {
        return new Review(REVIEW_1_ID, "Updated review", null);
    }
}
