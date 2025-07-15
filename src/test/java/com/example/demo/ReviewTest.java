package com.example.demo;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.demo.TestData.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReviewTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @WithUserDetails(value = "admin")
    void getAll_whenProductExists_shouldReturnReviews() throws Exception {
        mockMvc.perform(get("/api/products/" + PRODUCT_1_ID + "/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "admin")
    void get_whenReviewExists_shouldReturnReview() throws Exception {
        mockMvc.perform(get("/api/products/" + PRODUCT_1_ID + "/reviews/" + REVIEW_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(REVIEW_1_ID))
                .andExpect(jsonPath("$.text").value(REVIEW_1_TEXT));
    }

    @Test
    @WithUserDetails(value = "admin")
    void get_whenReviewNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/products/" + PRODUCT_1_ID + "/reviews/" + REVIEW_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "admin")
    void create_whenValidInput_shouldCreateReview() throws Exception {
        Review newReview = new Review();
        newReview.setText(REVIEW_NEW_TEXT);

        mockMvc.perform(post("/api/products/" + PRODUCT_1_ID + "/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReview)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.review_id").exists());
    }

    @Test
    @WithUserDetails(value = "admin")
    void create_whenInvalidInput_shouldReturn400() throws Exception {
        Review invalidReview = new Review();

        mockMvc.perform(post("/api/products/" + PRODUCT_1_ID + "/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReview)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "admin")
    void update_whenValidInput_shouldUpdateReview() throws Exception {
        String jsonReviewUpdated = objectMapper.writeValueAsString(getReviewUpdated());

        mockMvc.perform(put("/api/products/" + PRODUCT_1_ID + "/reviews/" + REVIEW_1_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonReviewUpdated))
                .andExpect(status().isNoContent());


        List<Review> reviews = reviewRepository.findByProductId(PRODUCT_1_ID);
        assertThat(reviews).anyMatch(p -> p.getText().equals(getReviewUpdated().getText()));
    }

    @Test
    @WithUserDetails(value = "admin")
    void delete_whenReviewExists_shouldDeleteReview() throws Exception {
        mockMvc.perform(delete("/api/products/" + PRODUCT_1_ID + "/reviews/" + REVIEW_1_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "admin")
    void delete_whenReviewNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/products/" + PRODUCT_1_ID + "/reviews/" + REVIEW_NOT_FOUND))
                .andExpect(status().isNotFound());
    }
}
