package com.listajuegos.service.external;

import com.listajuegos.entity.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PriceCheckService {

    private final RestTemplate restTemplate;

    @Value("${api.rawg.key}")
    private String rawgApiKey;

    @Value("${api.rawg.base-url}")
    private String rawgBaseUrl;

    public void checkSteamPrice(Wishlist item) {
        try {
            String url = "https://store.steampowered.com/api/appdetails?appids=" + extractSteamAppId(item.getSteamUrl());
            var response = restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            item.setOnSale(false);
            item.setSteamPrice(null);
        }
    }

    public void checkEnebaPrice(Wishlist item) {
        try {
            String url = "https://www.eneba.com/price-comparator/api/prices?q=" + item.getName();
            var response = restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            item.setEnebaPrice(null);
        }
    }

    private String extractSteamAppId(String steamUrl) {
        if (steamUrl == null || steamUrl.isEmpty()) return "0";
        String[] parts = steamUrl.split("/");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("app") && i + 1 < parts.length) {
                return parts[i + 1];
            }
        }
        return "0";
    }
}
