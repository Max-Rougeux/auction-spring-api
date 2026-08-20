package com.rougeux.auction.web.bid;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BidChartDto(
    int amount,
    Instant time,
    String user
) {}
