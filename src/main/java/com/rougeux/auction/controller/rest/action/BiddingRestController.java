package com.rougeux.auction.controller.rest.action;

import com.rougeux.auction.service.action.BiddingService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import com.rougeux.auction.web.request.BidRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
public class BiddingRestController {

    private final BiddingService service;
    private final ApiResponseFactory factory;

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<Void>> placeBid(
            @Valid @RequestBody BidRequest request,
            @AuthenticationPrincipal Jwt accessToken) {
        service.placeBid(request, accessToken.getSubject());

        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_OK_BID_SUBMIT, "bid.submit.success"));
    }
}
