package com.rougeux.auction.web.sale;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rougeux.auction.web.item.ItemDto;
import com.rougeux.auction.web.user.UserSummaryDto;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"slug", "state", "createdAt", "startedAt", "endedAt", "owner", "startingPrice", "currentPrice", "likes", "items"})
public record SaleDetailDto (
    String slug,
    Instant createdAt,
    Instant startedAt,
    Instant endedAt,
    int startingPrice,
    int currentPrice,
    int likes,
    String state,
    ItemDto item,
    UserSummaryDto owner
) {}
