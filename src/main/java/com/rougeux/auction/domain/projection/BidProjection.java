package com.rougeux.auction.domain.projection;

import com.rougeux.auction.domain.bo.*;
import lombok.Builder;

@Builder
public record BidProjection(
        Bid bid,
        String slug,
        UserProjection userProjection
) {}
