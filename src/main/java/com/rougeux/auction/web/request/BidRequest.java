package com.rougeux.auction.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record BidRequest(

        @Min(1)
        int amount,

        @NotBlank
        @Pattern(regexp = "^[a-z0-9][a-z0-9-]*-[a-f0-9]{8}$")
        String slug
) {}
