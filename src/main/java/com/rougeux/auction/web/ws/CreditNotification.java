package com.rougeux.auction.web.ws;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record CreditNotification(
        int amount,
        @Nullable String slug,
        @Nullable int outbid
) {
}
