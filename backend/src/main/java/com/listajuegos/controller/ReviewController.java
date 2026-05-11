package com.listajuegos.controller;

import com.listajuegos.dto.ReviewRequest;
import com.listajuegos.dto.ReviewResponse;
import com.listajuegos.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games/{gameId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long gameId, Authentication auth) {
        return ResponseEntity.ok(reviewService.getReview(gameId, getUserId(auth)));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long gameId,
            @Valid @RequestBody ReviewRequest request,
            Authentication auth) {
        return ResponseEntity.ok(reviewService.addReview(gameId, request, getUserId(auth)));
    }

    @PutMapping
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long gameId,
            @Valid @RequestBody ReviewRequest request,
            Authentication auth) {
        return ResponseEntity.ok(reviewService.updateReview(gameId, request, getUserId(auth)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReview(@PathVariable Long gameId, Authentication auth) {
        reviewService.deleteReview(gameId, getUserId(auth));
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getDetails();
    }
}
