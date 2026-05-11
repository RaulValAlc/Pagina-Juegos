package com.listajuegos.controller;

import com.listajuegos.dto.GameRequest;
import com.listajuegos.dto.GameResponse;
import com.listajuegos.enums.GameStatus;
import com.listajuegos.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<Page<GameResponse>> getGames(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Pageable pageable,
            Authentication auth) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(gameService.getUserGames(userId, status, search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(gameService.getGame(id, getUserId(auth)));
    }

    @PostMapping
    public ResponseEntity<GameResponse> addGame(@Valid @RequestBody GameRequest request, Authentication auth) {
        return ResponseEntity.ok(gameService.addGame(request, getUserId(auth)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGame(@PathVariable Long id, @Valid @RequestBody GameRequest request, Authentication auth) {
        return ResponseEntity.ok(gameService.updateGame(id, request, getUserId(auth)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id, Authentication auth) {
        gameService.deleteGame(id, getUserId(auth));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GameResponse> updateStatus(@PathVariable Long id, @RequestParam GameStatus status, Authentication auth) {
        return ResponseEntity.ok(gameService.updateGameStatus(id, status, getUserId(auth)));
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getDetails();
    }
}
