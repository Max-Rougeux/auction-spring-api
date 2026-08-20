package com.rougeux.auction.service.action;

import com.rougeux.auction.configuration.security.contract.UserPrincipal;
import com.rougeux.auction.domain.bo.RefreshToken;
import com.rougeux.auction.exception.UnauthorizedException;
import com.rougeux.auction.service.security.CustomUserDetailsService;
import com.rougeux.auction.service.security.AccessTokenService;
import com.rougeux.auction.service.security.RefreshTokenService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.request.LoginRequest;
import com.rougeux.auction.web.response.LoginResponse;
import com.rougeux.auction.web.result.AuthResult;
import com.rougeux.auction.web.result.AccessResult;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder encoder;
    private final AccessTokenService jwtService;
    private final RefreshTokenService refreshService;
    private final CustomUserDetailsService service;

    public AuthResult login(LoginRequest request) {
        UserPrincipal principal = service.loadUserByUsername(request.username());

        if (!encoder.matches(request.password(), principal.getPassword()))
            throw new BadCredentialsException("login.error.invalidCredentials");

        AccessResult result = jwtService.generateToken(principal);
        RefreshToken refreshToken = refreshService.create(principal.getUsername());

        return AuthResult.builder()
                .response(LoginResponse.builder()
                        .username(principal.getUsername())
                        .accessToken(result.token())
                        .expiresAt(result.expires_at())
                        .authorities(result.authorities())
                        .build())
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResult refresh(Cookie cookie) {
        if(cookie == null)
            throw new UnauthorizedException(ApiCodes.CD_ERR_UNAUTHORIZED, "refresh.error.missingToken");

        RefreshToken token = refreshService.verify(cookie);

        UserPrincipal principal = service.loadUserByUsername(token.getUsername());

        AccessResult result = jwtService.generateToken(principal);
        RefreshToken refreshToken = refreshService.rotate(token);

        return AuthResult.builder()
                .response(LoginResponse.builder()
                        .username(principal.getUsername())
                        .accessToken(result.token())
                        .expiresAt(result.expires_at())
                        .authorities(result.authorities())
                        .build())
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(Cookie cookie) {
        this.refreshService.clear(cookie);
    }
}
