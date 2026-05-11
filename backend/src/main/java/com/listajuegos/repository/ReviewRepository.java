package com.listajuegos.repository;

import com.listajuegos.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByGameIdAndUserId(Long gameId, Long userId);
    boolean existsByGameIdAndUserId(Long gameId, Long userId);
}
