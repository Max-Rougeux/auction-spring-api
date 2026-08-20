package com.rougeux.auction.domain.projection;

import com.rougeux.auction.domain.bo.*;
import lombok.Builder;

@Builder
public record SaleProjection(
        Sale sale,
        ItemProjection itemProjection,
        UserProjection userProjection
) { }
