package com.rougeux.auction.service.security;

import com.rougeux.auction.configuration.security.contract.UserPrincipal;
import com.rougeux.auction.web.result.AccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public AccessResult generateToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(15, ChronoUnit.MINUTES);
        List<String> authorities = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(principal.getUsername())
                .claim("roles", authorities)
                .build();

        JwtEncoderParameters params = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return AccessResult.builder()
                .token(encoder.encode(params).getTokenValue())
                .expires_at(expiresAt)
                .authorities(authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .map(a -> (GrantedAuthority) a)
                        .toList())
                .build();
    }

    public Instant getExpireAt(String token) {
        return decoder.decode(token)
                .getExpiresAt();
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        return decoder.decode(token)
                .<List<String>>getClaim("roles").stream()
                .map(SimpleGrantedAuthority::new)
                .map(a -> (GrantedAuthority) a)
                .toList();
    }
}
