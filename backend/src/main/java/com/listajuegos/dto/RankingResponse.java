package com.listajuegos.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class RankingResponse {
    private Long id;
    private String listName;
    private Integer position;
    private Long gameId;
    private String gameName;
    private String coverUrl;
}
