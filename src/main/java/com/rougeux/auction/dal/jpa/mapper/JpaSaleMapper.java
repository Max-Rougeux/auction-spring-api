package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.ItemEntity;
import com.rougeux.auction.dal.jpa.entity.SaleEntity;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.projection.SaleProjection;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaSaleMapper {

    public static SaleEntity from(Sale bo, ItemEntity item) {
        return SaleEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .slug(bo.getSlug())
                .createdAt(bo.getCreatedAt())
                .startedAt(bo.getStartedAt())
                .endedAt(bo.getEndedAt())
                .startingPrice(bo.getStartingPrice())
                .currentPrice(bo.getCurrentPrice())
                .likes(bo.getLikes())
                .state(bo.getState())
                .item(item)
                .build();
    }

    public static Sale toBo(SaleEntity entity) {
        return Sale.builder()
                .id(String.valueOf(entity.getId()))
                .slug(entity.getSlug())
                .createdAt(entity.getCreatedAt())
                .startedAt(entity.getStartedAt())
                .endedAt(entity.getEndedAt())
                .startingPrice(entity.getStartingPrice())
                .currentPrice(entity.getCurrentPrice())
                .likes(entity.getLikes())
                .state(entity.getState())
                .itemId(String.valueOf(entity.getItem().getId()))
                .build();
    }

    public static SaleProjection toProjection(SaleEntity entity) {
        return SaleProjection.builder()
                .sale(toBo(entity))
                .itemProjection(JpaItemMapper.toProjection(entity.getItem()))
                .userProjection(JpaUserMapper.toProjection(entity.getItem().getOwner()))
                .build();
    }
}
