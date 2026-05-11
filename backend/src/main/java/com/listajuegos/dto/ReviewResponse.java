package com.listajuegos.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String opinion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
}
