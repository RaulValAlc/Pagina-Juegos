package com.listajuegos.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class WishlistResponse {
    private Long id;
    private String name;
    private String coverUrl;
    private Double steamPrice;
    private Double enebaPrice;
    private Double originalPrice;
    private Boolean onSale;
    private Integer discountPercent;
    private String steamUrl;
    private String enebaUrl;
    private LocalDateTime createdAt;
}
