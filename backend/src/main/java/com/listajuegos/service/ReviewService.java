package com.listajuegos.service;

import com.listajuegos.dto.ReviewRequest;
import com.listajuegos.dto.ReviewResponse;
import com.listajuegos.entity.Game;
import com.listajuegos.entity.Review;
import com.listajuegos.entity.User;
import com.listajuegos.enums.GameStatus;
import com.listajuegos.exception.BadRequestException;
import com.listajuegos.exception.ResourceNotFoundException;
import com.listajuegos.repository.GameRepository;
import com.listajuegos.repository.ReviewRepository;
import com.listajuegos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse addReview(Long gameId, ReviewRequest request, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));

        if (!game.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Game", gameId);
        }

        if (game.getStatus() != GameStatus.PLAYED) {
            throw new BadRequestException("Can only review games with PLAYED status");
        }

        if (reviewRepository.existsByGameIdAndUserId(gameId, userId)) {
            throw new BadRequestException("Review already exists for this game");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Review review = Review.builder()
                .rating(request.getRating())
                .opinion(request.getOpinion())
                .game(game)
                .user(user)
                .build();

        game.setPersonalRating(request.getRating());
        review = reviewRepository.save(review);
        gameRepository.save(game);

        return toReviewResponse(review);
    }

    @Transactional
    public ReviewResponse updateReview(Long gameId, ReviewRequest request, Long userId) {
        Review review = reviewRepository.findByGameIdAndUserId(gameId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Review for game", gameId));

        review.setRating(request.getRating());
        review.setOpinion(request.getOpinion());

        Game game = review.getGame();
        game.setPersonalRating(request.getRating());

        review = reviewRepository.save(review);
        gameRepository.save(game);

        return toReviewResponse(review);
    }

    @Transactional
    public void deleteReview(Long gameId, Long userId) {
        Review review = reviewRepository.findByGameIdAndUserId(gameId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Review for game", gameId));

        Game game = review.getGame();
        game.setPersonalRating(null);
        game.setReview(null);

        reviewRepository.delete(review);
        gameRepository.save(game);
    }

    public ReviewResponse getReview(Long gameId, Long userId) {
        Review review = reviewRepository.findByGameIdAndUserId(gameId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Review for game", gameId));

        return toReviewResponse(review);
    }

    private ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .opinion(review.getOpinion())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .username(review.getUser().getUsername())
                .build();
    }
}
