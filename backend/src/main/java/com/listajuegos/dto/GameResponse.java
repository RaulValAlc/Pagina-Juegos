package com.listajuegos.dto;

import com.listajuegos.enums.GameStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class GameResponse {
    private Long id;
    private String name;
    private String description;
    private String genre;
    private String platform;
    private LocalDate releaseDate;
    private String coverUrl;
    private String developer;
    private Double globalRating;
    private GameStatus status;
    private Integer personalRating;
    private Integer rawgId;
    private LocalDateTime createdAt;
    private ReviewResponse review;
}
