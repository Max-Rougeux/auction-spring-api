package com.rougeux.auction.web.ws;

import lombok.Builder;

@Builder
public record PriceUpdate(
        String slug,
        int price
) {
}
