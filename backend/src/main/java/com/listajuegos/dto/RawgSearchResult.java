package com.listajuegos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RawgSearchResult {
    private int rawgId;
    private String name;
    private String released;
    private String coverUrl;
    private double rating;
    private String genres;
    private String platforms;
    private String developers;
}
