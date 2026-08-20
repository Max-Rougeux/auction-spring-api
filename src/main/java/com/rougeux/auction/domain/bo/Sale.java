package com.rougeux.auction.domain.bo;

import com.rougeux.auction.domain.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Sale {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String slug;
    private Instant createdAt;
    private Instant startedAt;
    private Instant endedAt;

    private int startingPrice;
    private int currentPrice;
    private int likes;
    private State state;

    private String itemId;
}

