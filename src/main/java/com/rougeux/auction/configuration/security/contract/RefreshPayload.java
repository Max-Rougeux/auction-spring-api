package com.rougeux.auction.configuration.security.contract;

import lombok.Builder;

@Builder
public record RefreshPayload(
        String publicId,
        String rawToken
) {
}
