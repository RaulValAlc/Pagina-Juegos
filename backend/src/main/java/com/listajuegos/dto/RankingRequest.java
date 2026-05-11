package com.listajuegos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RankingRequest {
    @NotBlank
    private String listName;

    @NotNull
    private Long gameId;

    private Integer position;
}
