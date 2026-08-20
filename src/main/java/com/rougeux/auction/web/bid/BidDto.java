package com.rougeux.auction.web.bid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rougeux.auction.web.user.UserSummaryDto;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BidDto (
    int amount,
    Instant time,
    String slug,
    UserSummaryDto bidder
) {}
