package com.rougeux.auction.mapper;

import com.rougeux.auction.domain.projection.SaleProjection;
import com.rougeux.auction.web.sale.SaleCardDto;
import com.rougeux.auction.web.sale.SaleDetailDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SaleMapper {

    public SaleCardDto toCardDto(SaleProjection agg) {
        return SaleCardDto.builder()
                .slug(agg.sale().getSlug())
                .startedAt(agg.sale().getStartedAt())
                .endedAt(agg.sale().getEndedAt())
                .startingPrice(agg.sale().getStartingPrice())
                .currentPrice(agg.sale().getCurrentPrice())
                .likes(agg.sale().getLikes())
                .state(agg.sale().getState().name())
                .item(ItemMapper.toCardDto(agg.itemProjection()))
                .build();
    }

    public SaleDetailDto toDetailDto(SaleProjection agg) {
        return SaleDetailDto.builder()
                .slug(agg.sale().getSlug())
                .createdAt(agg.sale().getCreatedAt())
                .startedAt(agg.sale().getStartedAt())
                .endedAt(agg.sale().getEndedAt())
                .startingPrice(agg.sale().getStartingPrice())
                .currentPrice(agg.sale().getCurrentPrice())
                .likes(agg.sale().getLikes())
                .state(agg.sale().getState().name())
                .item(ItemMapper.toDto(agg.itemProjection()))
                .owner(UserMapper.toSummaryDto(agg.userProjection()))
                .build();
    }
}
