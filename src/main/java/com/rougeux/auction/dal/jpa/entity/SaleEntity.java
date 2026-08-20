package com.rougeux.auction.dal.jpa.entity;

import com.rougeux.auction.domain.enums.State;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "SALES")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SaleEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String slug;
    private Instant createdAt;
    private Instant startedAt;
    private Instant endedAt;

    private int startingPrice;
    private int currentPrice;
    private int likes;

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;


}
