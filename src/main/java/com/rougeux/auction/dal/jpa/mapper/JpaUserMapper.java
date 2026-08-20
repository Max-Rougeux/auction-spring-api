package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.ImageEntity;
import com.rougeux.auction.dal.jpa.entity.UserEntity;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.projection.UserProjection;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaUserMapper {

    public static UserEntity from(User bo, ImageEntity image) {
        return UserEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .username(bo.getUsername())
                .password(bo.getPassword())
                .slug(bo.getSlug())
                .firstname(bo.getFirstname())
                .lastname(bo.getLastname())
                .phone(bo.getPhone())
                .credit(bo.getCredit())
                .roles(bo.getRoles())
                .enabled(bo.isEnabled())
                .createdAt(bo.getCreatedAt())
                .thumbnail(image)
                .build();
    }
    public static User toBo(UserEntity entity) {
        return User.builder()
                .id(String.valueOf(entity.getId()))
                .username(entity.getUsername())
                .password(entity.getPassword())
                .slug(entity.getSlug())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .phone(entity.getPhone())
                .credit(entity.getCredit())
                .roles(entity.getRoles())
                .enabled(entity.isEnabled())
                .createdAt(entity.getCreatedAt())
                .imageId(entity.getThumbnail() != null ? String.valueOf(entity.getThumbnail().getId()) : null)
                .build();
    }

    public static UserProjection toProjection(UserEntity entity) {
        return UserProjection.builder()
                .user(toBo(entity))
                .image(JpaImageMapper.toBo(entity.getThumbnail()))
                .build();
    }
}
