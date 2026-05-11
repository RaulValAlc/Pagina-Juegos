package com.listajuegos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rankings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "list_name", nullable = false, length = 100)
    private String listName;

    @Column(name = "position", nullable = false)
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
