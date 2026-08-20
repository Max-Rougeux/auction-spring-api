package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.RefreshTokenEntity;
import com.rougeux.auction.domain.bo.RefreshToken;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaRefreshMapper {

    public static RefreshTokenEntity from(RefreshToken bo) {
        return RefreshTokenEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .publicId(bo.getPublicId())
                .username(bo.getUsername())
                .hash(bo.getHash())
                .createdAt(bo.getCreatedAt())
                .expiresAt(bo.getExpiresAt())
                .build();
    }


    public static RefreshToken toBo(RefreshTokenEntity entity) {
        return RefreshToken.builder()
                .id(String.valueOf(entity.getId()))
                .publicId(entity.getPublicId())
                .username(entity.getUsername())
                .hash(entity.getHash())
                .createdAt(entity.getCreatedAt())
                .expiresAt(entity.getExpiresAt())
                .build();
    }
}
