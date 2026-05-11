package com.listajuegos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "steam_price")
    private Double steamPrice;

    @Column(name = "eneba_price")
    private Double enebaPrice;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "on_sale")
    private Boolean onSale;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "steam_url", length = 500)
    private String steamUrl;

    @Column(name = "eneba_url", length = 500)
    private String enebaUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
