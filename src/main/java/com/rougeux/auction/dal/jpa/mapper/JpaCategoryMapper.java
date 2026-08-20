package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.CategoryEntity;
import com.rougeux.auction.domain.bo.Category;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaCategoryMapper {

    public static CategoryEntity from(Category bo) {
        return CategoryEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .slug(bo.getSlug())
                .label(bo.getLabel())
                .build();
    }

    public static Category toBo(CategoryEntity entity) {
        return Category.builder()
                .id(String.valueOf(entity.getId()))
                .slug(entity.getSlug())
                .label(entity.getLabel())
                .build();
    }
}
