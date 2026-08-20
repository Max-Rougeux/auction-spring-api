package com.rougeux.auction.controller.rest.action;

import com.rougeux.auction.service.action.AuthService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.support.factory.CookieFactory;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import com.rougeux.auction.web.request.LoginRequest;
import com.rougeux.auction.web.response.LoginResponse;
import com.rougeux.auction.web.result.AuthResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService service;
    private final ApiResponseFactory factory;
    private final CookieFactory cookieFactory;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        AuthResult result = service.login(request);
        response.addCookie(cookieFactory.refreshCookie(result.refreshToken()));

        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_OK_USER_AUTHENTICATED, "login.success", result.response()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(name = "refresh_token", required = false) Cookie cookie, HttpServletResponse response) {

        AuthResult result = service.refresh(cookie);
        response.addCookie(cookieFactory.refreshCookie(result.refreshToken()));

        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_OK_USER_REFRESH, "refresh.success", result.response()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refresh_token", required = false) Cookie cookie, HttpServletResponse response) {

        if(cookie != null)
            service.logout(cookie);
        response.addCookie(cookieFactory.expiredCookie());

        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_OK_USER_LOGOUT, "logout.success"));
    }
}
