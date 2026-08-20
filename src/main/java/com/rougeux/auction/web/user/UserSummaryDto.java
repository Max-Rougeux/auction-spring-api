package com.rougeux.auction.web.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rougeux.auction.web.image.ImageDto;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserSummaryDto (
    String slug,
    String firstname,
    String lastname,
    Instant createdAt,
    ImageDto thumbnail
) {}
