package com.rougeux.auction.dal.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "BIDS")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class BidEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private int amount;
    private Instant time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private SaleEntity sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity bidder;

}
