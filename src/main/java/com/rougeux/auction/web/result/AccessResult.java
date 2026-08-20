package com.rougeux.auction.web.result;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.List;

@Builder
public record AccessResult(
        String token,
        Instant expires_at,
        List<GrantedAuthority> authorities
) {
}
