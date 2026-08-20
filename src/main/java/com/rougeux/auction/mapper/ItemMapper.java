package com.rougeux.auction.mapper;

import com.rougeux.auction.domain.projection.ItemProjection;
import com.rougeux.auction.web.item.ItemCardDto;
import com.rougeux.auction.web.item.ItemDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemMapper {

    public ItemCardDto toCardDto(ItemProjection agg) {
        return new ItemCardDto(
                agg.brand().getName(),
                agg.item().getModel(),
                agg.item().isGem(),
                CategoryMapper.toDto(agg.category()),
                ImageMapper.toDto(agg.image()));
    }

    public ItemDto toDto(ItemProjection agg) {
        return new ItemDto(
                agg.brand().getName(),
                agg.item().getModel(),
                agg.item().getDescription(),
                agg.item().getCondition().name(),
                agg.item().getYear(),
                agg.item().isGem(),
                CategoryMapper.toDto(agg.category()),
                ImageMapper.toDto(agg.image()));
    }
}
