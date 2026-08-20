package com.rougeux.auction.web.result;

import com.rougeux.auction.domain.bo.RefreshToken;
import com.rougeux.auction.web.response.LoginResponse;
import lombok.Builder;

@Builder
public record AuthResult(
        LoginResponse response,
        RefreshToken refreshToken
) {
}
