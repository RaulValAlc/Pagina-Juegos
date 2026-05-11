package com.listajuegos.dto;

import com.listajuegos.enums.GameStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class GameRequest {
    @NotBlank
    private String name;

    private String description;
    private String genre;
    private String platform;
    private LocalDate releaseDate;
    private String coverUrl;
    private String developer;
    private Double globalRating;

    @NotNull
    private GameStatus status;

    private Integer personalRating;
    private Integer rawgId;
}
