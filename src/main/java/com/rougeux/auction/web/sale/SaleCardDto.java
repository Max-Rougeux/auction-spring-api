package com.rougeux.auction.web.sale;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rougeux.auction.web.item.ItemCardDto;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"slug", "state", "startedAt", "endedAt", "startingPrice", "currentPrice", "likes", "items"})
public record SaleCardDto(
        String slug,
        Instant startedAt,
        Instant endedAt,
        int startingPrice,
        int currentPrice,
        int likes,
        String state,
        ItemCardDto item
) {}
