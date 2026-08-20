package com.rougeux.auction.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String publicId;
    private String username;
    private String hash;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

    private String rawToken;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public long maxAgeSeconds() {
        return ChronoUnit.SECONDS.between(Instant.now(), expiresAt);
    }
}
