package com.listajuegos.service;

import com.listajuegos.dto.GameRequest;
import com.listajuegos.dto.GameResponse;
import com.listajuegos.dto.ReviewResponse;
import com.listajuegos.entity.Game;
import com.listajuegos.entity.Review;
import com.listajuegos.entity.User;
import com.listajuegos.enums.GameStatus;
import com.listajuegos.exception.ResourceNotFoundException;
import com.listajuegos.repository.GameRepository;
import com.listajuegos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public Page<GameResponse> getUserGames(Long userId, String status, String search, Pageable pageable) {
        Page<Game> games;

        if (search != null && !search.isEmpty()) {
            games = gameRepository.searchByUserId(userId, search, pageable);
        } else if (status != null && !status.isEmpty()) {
            games = gameRepository.findByUserIdAndStatus(userId, GameStatus.valueOf(status.toUpperCase()), pageable);
        } else {
            games = gameRepository.findByUserId(userId, pageable);
        }

        return games.map(this::toGameResponse);
    }

    public GameResponse getGame(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));

        if (!game.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Game", gameId);
        }

        return toGameResponse(game);
    }

    @Transactional
    public GameResponse addGame(GameRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Game game = Game.builder()
                .name(request.getName())
                .description(request.getDescription())
                .genre(request.getGenre())
                .platform(request.getPlatform())
                .releaseDate(request.getReleaseDate())
                .coverUrl(request.getCoverUrl())
                .developer(request.getDeveloper())
                .globalRating(request.getGlobalRating())
                .status(request.getStatus())
                .personalRating(request.getPersonalRating())
                .rawgId(request.getRawgId())
                .user(user)
                .build();

        game = gameRepository.save(game);
        return toGameResponse(game);
    }

    @Transactional
    public GameResponse updateGame(Long gameId, GameRequest request, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));

        if (!game.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Game", gameId);
        }

        game.setName(request.getName());
        game.setDescription(request.getDescription());
        game.setGenre(request.getGenre());
        game.setPlatform(request.getPlatform());
        game.setReleaseDate(request.getReleaseDate());
        game.setCoverUrl(request.getCoverUrl());
        game.setDeveloper(request.getDeveloper());
        game.setGlobalRating(request.getGlobalRating());
        game.setStatus(request.getStatus());
        game.setPersonalRating(request.getPersonalRating());
        game.setRawgId(request.getRawgId());

        game = gameRepository.save(game);
        return toGameResponse(game);
    }

    @Transactional
    public void deleteGame(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));

        if (!game.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Game", gameId);
        }

        gameRepository.delete(game);
    }

    @Transactional
    public GameResponse updateGameStatus(Long gameId, GameStatus status, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));

        if (!game.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Game", gameId);
        }

        game.setStatus(status);
        if (status != GameStatus.PLAYED) {
            game.setPersonalRating(null);
        }

        game = gameRepository.save(game);
        return toGameResponse(game);
    }

    private GameResponse toGameResponse(Game game) {
        ReviewResponse reviewResponse = null;
        if (game.getReview() != null) {
            Review r = game.getReview();
            reviewResponse = ReviewResponse.builder()
                    .id(r.getId())
                    .rating(r.getRating())
                    .opinion(r.getOpinion())
                    .createdAt(r.getCreatedAt())
                    .updatedAt(r.getUpdatedAt())
                    .username(game.getUser().getUsername())
                    .build();
        }

        return GameResponse.builder()
                .id(game.getId())
                .name(game.getName())
                .description(game.getDescription())
                .genre(game.getGenre())
                .platform(game.getPlatform())
                .releaseDate(game.getReleaseDate())
                .coverUrl(game.getCoverUrl())
                .developer(game.getDeveloper())
                .globalRating(game.getGlobalRating())
                .status(game.getStatus())
                .personalRating(game.getPersonalRating())
                .rawgId(game.getRawgId())
                .createdAt(game.getCreatedAt())
                .review(reviewResponse)
                .build();
    }
}
