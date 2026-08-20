package com.rougeux.auction.domain.projection;

import com.rougeux.auction.domain.bo.*;
import lombok.Builder;


@Builder
public record ItemProjection(
        Item item,
        Brand brand,
        Category category,
        Image image
) {}
