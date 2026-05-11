package com.listajuegos.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class DashboardResponse {
    private long totalGames;
    private long playedGames;
    private long pendingGames;
    private long wishedGames;
    private long purchasedGames;
    private long abandonedGames;
    private List<GameResponse> recentGames;
    private List<GameResponse> topRated;
    private List<WishlistResponse> wishlistItems;
}
