package com.rougeux.auction.mapper;

import com.rougeux.auction.domain.enums.Role;
import com.rougeux.auction.domain.projection.UserProjection;
import com.rougeux.auction.web.user.UserDto;
import com.rougeux.auction.web.user.UserSummaryDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public UserDto toDto(UserProjection projection) {
        return UserDto.builder()
                .slug(projection.user().getSlug())
                .username(projection.user().getUsername())
                .firstname(projection.user().getFirstname())
                .lastname(projection.user().getLastname())
                .phone(projection.user().getPhone())
                .credit(projection.user().getCredit())
                .createdAt(projection.user().getCreatedAt())
                .isAdmin(projection.user().getRoles().contains(Role.ADMIN))
                .thumbnail(ImageMapper.toDto(projection.image()))
                .build();
    }

    public UserSummaryDto toSummaryDto(UserProjection projection) {
        return UserSummaryDto.builder()
                .slug(projection.user().getSlug())
                .firstname(projection.user().getFirstname())
                .lastname(projection.user().getLastname())
                .createdAt(projection.user().getCreatedAt())
                .thumbnail(ImageMapper.toDto(projection.image()))
                .build();
    }
}
