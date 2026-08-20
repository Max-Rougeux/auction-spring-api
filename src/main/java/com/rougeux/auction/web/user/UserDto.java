package com.rougeux.auction.web.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rougeux.auction.web.image.ImageDto;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto (
        String slug,
        String username,
        String firstname,
        String lastname,
        String phone,
        int credit,
        boolean isAdmin,
        Instant createdAt,
        ImageDto thumbnail
) {}
