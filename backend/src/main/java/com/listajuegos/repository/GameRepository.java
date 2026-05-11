package com.listajuegos.repository;

import com.listajuegos.entity.Game;
import com.listajuegos.enums.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    Page<Game> findByUserId(Long userId, Pageable pageable);

    Page<Game> findByUserIdAndStatus(Long userId, GameStatus status, Pageable pageable);

    List<Game> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, GameStatus status);

    @Query("SELECT g FROM Game g WHERE g.user.id = :userId AND " +
           "LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Game> searchByUserId(@Param("userId") Long userId, @Param("query") String query, Pageable pageable);

    @Query("SELECT g FROM Game g WHERE g.user.id = :userId AND g.personalRating IS NOT NULL " +
           "ORDER BY g.personalRating DESC")
    List<Game> findTopRatedByUserId(@Param("userId") Long userId, Pageable pageable);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, GameStatus status);

    List<Game> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    boolean existsByIdAndUserId(Long id, Long userId);
}
