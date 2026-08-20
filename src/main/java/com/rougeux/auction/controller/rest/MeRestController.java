package com.rougeux.auction.controller.rest;

import com.rougeux.auction.service.MeService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import com.rougeux.auction.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class MeRestController {

    private final MeService service;
    private final ApiResponseFactory factory;

    @GetMapping
    public ResponseEntity<ApiResponse<UserDto>> getProfile(
            @AuthenticationPrincipal Jwt accessToken) {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "me.profile.success",
                        service.getProfile(accessToken.getSubject())));
    }
}
