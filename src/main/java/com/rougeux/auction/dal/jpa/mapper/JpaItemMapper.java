package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.*;
import com.rougeux.auction.domain.bo.Item;
import com.rougeux.auction.domain.projection.ItemProjection;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaItemMapper {

    public static ItemEntity from(Item bo,
                                  ImageEntity thumbnail,
                                  BrandEntity brand,
                                  CategoryEntity category,
                                  UserEntity owner) {
        return ItemEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .model(bo.getModel())
                .description(bo.getDescription())
                .year(bo.getYear())
                .isGem(bo.isGem())
                .condition(bo.getCondition())
                .thumbnail(thumbnail)
                .brand(brand)
                .category(category)
                .owner(owner)
                .build();
    }

    public static Item toBo(ItemEntity entity) {
        return Item.builder()
                .id(String.valueOf(entity.getId()))
                .model(entity.getModel())
                .description(entity.getDescription())
                .year(entity.getYear())
                .isGem(entity.isGem())
                .condition(entity.getCondition())
                .imageId(entity.getThumbnail().getId().toString())
                .brandId(String.valueOf(entity.getBrand().getId()))
                .categoryId(String.valueOf(entity.getCategory().getId()))
                .userId(String.valueOf(entity.getOwner().getId()))
                .build();
    }

    public static ItemProjection toProjection(ItemEntity entity) {
        return ItemProjection.builder()
                .item(toBo(entity))
                .image(JpaImageMapper.toBo(entity.getThumbnail()))
                .brand(JpaBrandMapper.toBo(entity.getBrand()))
                .category(JpaCategoryMapper.toBo(entity.getCategory()))
                .build();
    }
}
