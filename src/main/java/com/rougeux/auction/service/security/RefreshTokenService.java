package com.rougeux.auction.service.security;

import com.rougeux.auction.configuration.security.contract.RefreshPayload;
import com.rougeux.auction.dal.RefreshTokenDao;
import com.rougeux.auction.domain.bo.RefreshToken;
import com.rougeux.auction.exception.InternalException;
import com.rougeux.auction.exception.UnauthorizedException;
import com.rougeux.auction.support.ApiCodes;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

import static com.rougeux.auction.support.ApiCodes.CD_ERR_UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenDao repository;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public RefreshToken create(String username) {
        String raw = UUID.randomUUID().toString();
        Instant now = Instant.now();

        byte[] bytes = new byte[16];
        SECURE_RANDOM.nextBytes(bytes);

        RefreshToken token = RefreshToken.builder()
                .publicId(Base64.getUrlEncoder().withoutPadding().encodeToString(bytes))
                .username(username)
                .hash(encode(raw))
                .createdAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .rawToken(raw)
                .build();

        repository.save(token);
        return token;
    }

    public RefreshToken verify(Cookie cookie) {
        RefreshPayload payload = this.extract(cookie);
        RefreshToken token = repository.findByPublicId(payload.publicId())
                .orElseThrow(() -> new UnauthorizedException(CD_ERR_UNAUTHORIZED, "refresh.error.invalidToken"));

        if (token.isExpired()) {
            this.delete(token.getPublicId());
            throw new UnauthorizedException(CD_ERR_UNAUTHORIZED, "refresh.error.invalidToken");
        }

        if (!encode(payload.rawToken()).equals(token.getHash())) {
            this.delete(token.getPublicId());
            throw new UnauthorizedException(CD_ERR_UNAUTHORIZED, "refresh.error.invalidToken");
        }

        return token;
    }

    @Transactional
    public RefreshToken rotate(RefreshToken old) {
        String username = old.getUsername();
        this.delete(old.getPublicId());

        return create(username);
    }

    public void delete(String publicId) {
        repository.delete(publicId);
    }

    public void clear(Cookie cookie) {
        this.delete(this.extract(cookie).publicId());
    }

    private RefreshPayload extract(Cookie cookie) {
        if (cookie == null || !cookie.getValue().contains("."))
            throw new UnauthorizedException(CD_ERR_UNAUTHORIZED, "refresh.error.invalidToken");

        String[] parts = cookie.getValue().split("\\.", 2);

        return RefreshPayload.builder()
                .publicId(parts[0])
                .rawToken(parts[1])
                .build();
    }

    private String encode(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(digest.digest(raw.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException(ApiCodes.CD_INTERNAL_ERROR, "request.internal.error");
        }
    }
}
