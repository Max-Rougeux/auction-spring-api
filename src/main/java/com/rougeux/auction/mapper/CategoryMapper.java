package com.rougeux.auction.mapper;

import com.rougeux.auction.domain.bo.Category;
import com.rougeux.auction.web.category.CategoryCountDto;
import com.rougeux.auction.web.category.CategoryDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .slug(category.getSlug())
                .label(category.getLabel())
                .build();
    }
    public CategoryCountDto toCountDto(Category category) {
        return CategoryCountDto.builder()
                .slug(category.getSlug())
                .label(category.getLabel())
                .count(category.getCount())
                .build();
    }
}
