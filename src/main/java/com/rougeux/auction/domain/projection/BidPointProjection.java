package com.rougeux.auction.domain.projection;

import lombok.Builder;

import java.time.Instant;

@Builder
public record BidPointProjection(
        int amount,
        Instant time,
        String user
) {}
