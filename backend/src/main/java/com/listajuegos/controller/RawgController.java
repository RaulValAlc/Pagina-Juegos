package com.listajuegos.controller;

import com.listajuegos.dto.RawgSearchResult;
import com.listajuegos.service.external.RawgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rawg")
@RequiredArgsConstructor
public class RawgController {

    private final RawgService rawgService;

    @GetMapping("/search")
    public ResponseEntity<List<RawgSearchResult>> searchGames(@RequestParam String query) {
        return ResponseEntity.ok(rawgService.searchGames(query));
    }
}
