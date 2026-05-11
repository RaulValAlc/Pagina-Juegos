package com.listajuegos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class RawgApiResponse {
    private List<RawgGame> results;

    @Data
    public static class RawgGame {
        private int id;
        private String name;
        private String released;
        @JsonProperty("background_image")
        private String backgroundImage;
        private double rating;
        private List<Genre> genres;
        private List<PlatformEntry> platforms;
        private List<Developer> developers;
    }

    @Data
    public static class Genre {
        private String name;
    }

    @Data
    public static class PlatformEntry {
        private Platform platform;
    }

    @Data
    public static class Platform {
        private String name;
    }

    @Data
    public static class Developer {
        private String name;
    }
}
