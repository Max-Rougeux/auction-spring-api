package com.rougeux.auction.domain.projection;

import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.bo.User;
import lombok.Builder;

@Builder
public record UserProjection (
        User user,
        Image image
) {}
