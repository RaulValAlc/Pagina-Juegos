package com.listajuegos.controller;

import com.listajuegos.dto.RankingRequest;
import com.listajuegos.dto.RankingResponse;
import com.listajuegos.service.RankingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingResponse>> getRankings(
            @RequestParam(required = false) String listName,
            Authentication auth) {
        return ResponseEntity.ok(rankingService.getUserRankings(getUserId(auth), listName));
    }

    @PostMapping
    public ResponseEntity<RankingResponse> addToRanking(@Valid @RequestBody RankingRequest request, Authentication auth) {
        return ResponseEntity.ok(rankingService.addToRanking(request, getUserId(auth)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromRanking(@PathVariable Long id, Authentication auth) {
        rankingService.removeFromRanking(id, getUserId(auth));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reorder")
    public ResponseEntity<Void> reorderRanking(
            @PathVariable Long id,
            @RequestParam int position,
            Authentication auth) {
        rankingService.reorderRanking(id, position, getUserId(auth));
        return ResponseEntity.ok().build();
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getDetails();
    }
}
