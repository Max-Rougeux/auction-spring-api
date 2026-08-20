package com.rougeux.auction.web.response;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.List;

@Builder
public record LoginResponse(
        String username,
        String accessToken,
        Instant expiresAt,
        List<GrantedAuthority> authorities
) {
    public static class LoginResponseBuilder {
        public LoginResponseBuilder accessToken(String token) {
            this.accessToken = token == null ? null : "Bearer " + token;
            return this;
        }
    }
}
