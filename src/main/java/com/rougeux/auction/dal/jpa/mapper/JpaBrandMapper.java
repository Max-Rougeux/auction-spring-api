package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.BrandEntity;
import com.rougeux.auction.domain.bo.Brand;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaBrandMapper {

    public static BrandEntity from(Brand bo) {
        return BrandEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .name(bo.getName())
                .build();
    }

    public static Brand toBo(BrandEntity entity) {
        return Brand.builder()
                .id(String.valueOf(entity.getId()))
                .name(entity.getName())
                .build();
    }
}
