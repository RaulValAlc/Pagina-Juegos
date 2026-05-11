package com.listajuegos.repository;

import com.listajuegos.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    List<Ranking> findByUserIdAndListNameOrderByPositionAsc(Long userId, String listName);
    List<Ranking> findByUserId(Long userId);
    Optional<Ranking> findByUserIdAndListNameAndGameId(Long userId, String listName, Long gameId);
    boolean existsByIdAndUserId(Long id, Long userId);
    void deleteByUserIdAndListName(Long userId, String listName);
}
