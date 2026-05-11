package com.listajuegos.service;

import com.listajuegos.dto.DashboardResponse;
import com.listajuegos.dto.GameResponse;
import com.listajuegos.dto.WishlistResponse;
import com.listajuegos.enums.GameStatus;
import com.listajuegos.repository.GameRepository;
import com.listajuegos.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final GameRepository gameRepository;
    private final WishlistRepository wishlistRepository;

    public DashboardResponse getDashboard(Long userId) {
        long totalGames = gameRepository.countByUserId(userId);
        long playedGames = gameRepository.countByUserIdAndStatus(userId, GameStatus.PLAYED);
        long pendingGames = gameRepository.countByUserIdAndStatus(userId, GameStatus.PENDING);
        long wishedGames = gameRepository.countByUserIdAndStatus(userId, GameStatus.WISHED);
        long purchasedGames = gameRepository.countByUserIdAndStatus(userId, GameStatus.PURCHASED);
        long abandonedGames = gameRepository.countByUserIdAndStatus(userId, GameStatus.ABANDONED);

        List<GameResponse> recentGames = gameRepository
                .findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 5))
                .stream()
                .map(g -> GameResponse.builder()
                        .id(g.getId()).name(g.getName()).coverUrl(g.getCoverUrl())
                        .status(g.getStatus()).personalRating(g.getPersonalRating())
                        .createdAt(g.getCreatedAt()).build())
                .collect(Collectors.toList());

        List<GameResponse> topRated = gameRepository
                .findTopRatedByUserId(userId, PageRequest.of(0, 5))
                .stream()
                .map(g -> GameResponse.builder()
                        .id(g.getId()).name(g.getName()).coverUrl(g.getCoverUrl())
                        .status(g.getStatus()).personalRating(g.getPersonalRating())
                        .build())
                .collect(Collectors.toList());

        List<WishlistResponse> wishlistItems = wishlistRepository.findByUserId(userId)
                .stream()
                .map(w -> WishlistResponse.builder()
                        .id(w.getId()).name(w.getName()).coverUrl(w.getCoverUrl())
                        .steamPrice(w.getSteamPrice()).enebaPrice(w.getEnebaPrice())
                        .onSale(w.getOnSale()).build())
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .totalGames(totalGames)
                .playedGames(playedGames)
                .pendingGames(pendingGames)
                .wishedGames(wishedGames)
                .purchasedGames(purchasedGames)
                .abandonedGames(abandonedGames)
                .recentGames(recentGames)
                .topRated(topRated)
                .wishlistItems(wishlistItems)
                .build();
    }
}
