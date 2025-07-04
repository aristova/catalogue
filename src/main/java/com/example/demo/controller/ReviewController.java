package com.example.demo.controller;

import com.example.demo.entity.Review;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.ReviewService;
import com.example.demo.util.MessageLocalizer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ReviewController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {

    public static final String REST_URL = "/api/products/{product_id}/reviews";

    private final ReviewService service;

    private final MessageLocalizer localizer;

    @GetMapping
    public List<Review> all(@PathVariable Integer product_id) {
        log.info("All reviews");
        return service.findByProduct(product_id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid Review review,
                                         @PathVariable Integer product_id,
                                         HttpServletRequest request) {
        Review saved = service.create(review, product_id);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", localizer.getMessage("review.added.success"),
                "ip", request.getRemoteAddr(),
                "review_id", saved.getId(),
                "review text", saved.getText()));
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable Integer product_id, @PathVariable Integer id) {
        return service.find(id, product_id)
                .orElseThrow(() -> new NotFoundException(localizer.getMessage("review.not.found", id)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody @Valid Review newReview,  @PathVariable Integer product_id, @PathVariable Integer id) {
       service.update(newReview, id, product_id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id, @PathVariable Integer product_id) {
        service.delete(id, product_id);
    }
}
