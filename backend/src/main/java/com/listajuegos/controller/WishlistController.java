package com.listajuegos.controller;

import com.listajuegos.dto.WishlistRequest;
import com.listajuegos.dto.WishlistResponse;
import com.listajuegos.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist(Authentication auth) {
        return ResponseEntity.ok(wishlistService.getUserWishlist(getUserId(auth)));
    }

    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(@Valid @RequestBody WishlistRequest request, Authentication auth) {
        return ResponseEntity.ok(wishlistService.addToWishlist(request, getUserId(auth)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long id, Authentication auth) {
        wishlistService.removeFromWishlist(id, getUserId(auth));
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getDetails();
    }
}
