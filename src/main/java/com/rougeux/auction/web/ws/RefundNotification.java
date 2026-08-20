package com.rougeux.auction.web.ws;

import lombok.Builder;

@Builder
public record RefundNotification(
        int refund,
        String slug,
        int amount
) {
}
