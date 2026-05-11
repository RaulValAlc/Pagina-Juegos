package com.listajuegos.service;

import com.listajuegos.dto.RankingRequest;
import com.listajuegos.dto.RankingResponse;
import com.listajuegos.entity.Game;
import com.listajuegos.entity.Ranking;
import com.listajuegos.entity.User;
import com.listajuegos.exception.BadRequestException;
import com.listajuegos.exception.ResourceNotFoundException;
import com.listajuegos.repository.GameRepository;
import com.listajuegos.repository.RankingRepository;
import com.listajuegos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public List<RankingResponse> getUserRankings(Long userId, String listName) {
        List<Ranking> rankings;
        if (listName != null && !listName.isEmpty()) {
            rankings = rankingRepository.findByUserIdAndListNameOrderByPositionAsc(userId, listName);
        } else {
            rankings = rankingRepository.findByUserId(userId);
        }
        return rankings.stream().map(this::toRankingResponse).collect(Collectors.toList());
    }

    @Transactional
    public RankingResponse addToRanking(RankingRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Game", request.getGameId()));

        if (!game.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Game", request.getGameId());
        }

        if (rankingRepository.findByUserIdAndListNameAndGameId(userId, request.getListName(), request.getGameId()).isPresent()) {
            throw new BadRequestException("Game already in this ranking list");
        }

        int position = request.getPosition() != null ? request.getPosition() :
                (int) rankingRepository.findByUserIdAndListNameOrderByPositionAsc(userId, request.getListName()).size() + 1;

        Ranking ranking = Ranking.builder()
                .listName(request.getListName())
                .position(position)
                .user(user)
                .game(game)
                .build();

        ranking = rankingRepository.save(ranking);
        return toRankingResponse(ranking);
    }

    @Transactional
    public void removeFromRanking(Long rankingId, Long userId) {
        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ranking", rankingId));

        if (!ranking.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Ranking", rankingId);
        }

        rankingRepository.delete(ranking);
    }

    @Transactional
    public void reorderRanking(Long rankingId, int newPosition, Long userId) {
        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ranking", rankingId));

        if (!ranking.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Ranking", rankingId);
        }

        ranking.setPosition(newPosition);
        rankingRepository.save(ranking);
    }

    private RankingResponse toRankingResponse(Ranking ranking) {
        return RankingResponse.builder()
                .id(ranking.getId())
                .listName(ranking.getListName())
                .position(ranking.getPosition())
                .gameId(ranking.getGame().getId())
                .gameName(ranking.getGame().getName())
                .coverUrl(ranking.getGame().getCoverUrl())
                .build();
    }
}
