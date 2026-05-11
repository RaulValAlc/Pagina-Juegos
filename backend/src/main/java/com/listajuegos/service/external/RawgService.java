package com.listajuegos.service.external;

import com.listajuegos.dto.GameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RawgService {

    private final RestTemplate restTemplate;

    @Value("${api.rawg.key}")
    private String apiKey;

    @Value("${api.rawg.base-url}")
    private String baseUrl;

    public String searchGames(String query) {
        String url = String.format("%s/games?key=%s&search=%s&page_size=10",
                baseUrl, apiKey, query);
        return restTemplate.getForObject(url, String.class);
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
