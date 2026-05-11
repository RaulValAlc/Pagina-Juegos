package com.listajuegos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WishlistRequest {
    @NotBlank
    private String name;

    private String coverUrl;
    private String steamUrl;
    private String enebaUrl;
}
