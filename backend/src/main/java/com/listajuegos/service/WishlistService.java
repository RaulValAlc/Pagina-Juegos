package com.listajuegos.service;

import com.listajuegos.dto.WishlistRequest;
import com.listajuegos.dto.WishlistResponse;
import com.listajuegos.entity.User;
import com.listajuegos.entity.Wishlist;
import com.listajuegos.exception.ResourceNotFoundException;
import com.listajuegos.repository.UserRepository;
import com.listajuegos.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    public List<WishlistResponse> getUserWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(this::toWishlistResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public WishlistResponse addToWishlist(WishlistRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Wishlist item = Wishlist.builder()
                .name(request.getName())
                .coverUrl(request.getCoverUrl())
                .steamUrl(request.getSteamUrl())
                .enebaUrl(request.getEnebaUrl())
                .user(user)
                .build();

        item = wishlistRepository.save(item);
        return toWishlistResponse(item);
    }

    @Transactional
    public void removeFromWishlist(Long wishlistId, Long userId) {
        Wishlist item = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item", wishlistId));

        if (!item.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Wishlist item", wishlistId);
        }

        wishlistRepository.delete(item);
    }

    private WishlistResponse toWishlistResponse(Wishlist item) {
        return WishlistResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .coverUrl(item.getCoverUrl())
                .steamPrice(item.getSteamPrice())
                .enebaPrice(item.getEnebaPrice())
                .originalPrice(item.getOriginalPrice())
                .onSale(item.getOnSale())
                .discountPercent(item.getDiscountPercent())
                .steamUrl(item.getSteamUrl())
                .enebaUrl(item.getEnebaUrl())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
