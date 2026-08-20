package com.rougeux.auction.event;

import com.rougeux.auction.domain.projection.BidProjection;
import lombok.Builder;

@Builder
public record BidPlacedEvent(
    BidProjection bid
) {
}
