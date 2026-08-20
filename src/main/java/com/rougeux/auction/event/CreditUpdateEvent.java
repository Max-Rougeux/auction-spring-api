package com.rougeux.auction.event;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record CreditUpdateEvent(
        int amount,
        String username,
        @Nullable String slug,
        @Nullable int outbid,
        boolean refund
) {}