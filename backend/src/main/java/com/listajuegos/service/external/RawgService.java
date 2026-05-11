package com.listajuegos.service.external;

import com.listajuegos.dto.GameRequest;
import com.listajuegos.dto.RawgApiResponse;
import com.listajuegos.dto.RawgSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawgService {

    private final RestTemplate restTemplate;

    @Value("${api.rawg.key}")
    private String apiKey;

    @Value("${api.rawg.base-url}")
    private String baseUrl;

    public List<RawgSearchResult> searchGames(String query) {
        String url = String.format("%s/games?key=%s&search=%s&page_size=5",
                baseUrl, apiKey, query);
        RawgApiResponse response = restTemplate.getForObject(url, RawgApiResponse.class);
        if (response == null || response.getResults() == null) return List.of();
        return response.getResults().stream()
                .map(this::toSearchResult)
                .collect(Collectors.toList());
    }

    private RawgSearchResult toSearchResult(RawgApiResponse.RawgGame game) {
        return RawgSearchResult.builder()
                .rawgId(game.getId())
                .name(game.getName())
                .released(game.getReleased())
                .coverUrl(game.getBackgroundImage())
                .rating(game.getRating())
                .genres(game.getGenres() != null ?
                    game.getGenres().stream().map(RawgApiResponse.Genre::getName).collect(Collectors.joining(", ")) : "")
                .platforms(game.getPlatforms() != null ?
                    game.getPlatforms().stream()
                        .map(p -> p.getPlatform().getName())
                        .collect(Collectors.joining(", ")) : "")
                .developers(game.getDevelopers() != null ?
                    game.getDevelopers().stream().map(RawgApiResponse.Developer::getName).collect(Collectors.joining(", ")) : "")
                .build();
    }

    public String getGameDetails(int rawgId) {
        String url = String.format("%s/games/%d?key=%s", baseUrl, rawgId, apiKey);
        return restTemplate.getForObject(url, String.class);
    }

    public GameRequest enrichFromRawg(GameRequest request) {
        if (request.getRawgId() == null) return request;

        try {
            String details = getGameDetails(request.getRawgId());
        } catch (Exception e) {
            // If RAWG API fails, return original request
        }

        return request;
    }
}
