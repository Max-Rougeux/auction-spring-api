package com.rougeux.auction.support.factory;


import com.rougeux.auction.domain.bo.RefreshToken;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieFactory {

    public Cookie refreshCookie(RefreshToken token) {
        Cookie cookie = new Cookie("refresh_token",
                token.getPublicId() + "." + token.getRawToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // TODO: setSecure(true) for production
        cookie.setPath("/api/auth");
        cookie.setMaxAge((int) token.maxAgeSeconds());
        return cookie;
    }

    public Cookie expiredCookie() {
        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/api/auth");
        return cookie;
    }
}
