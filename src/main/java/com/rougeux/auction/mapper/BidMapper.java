package com.rougeux.auction.mapper;

import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.web.bid.BidChartDto;
import com.rougeux.auction.web.bid.BidDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BidMapper {

    public BidDto toDto(BidProjection projection) {
        return BidDto.builder()
                .amount(projection.bid().getAmount())
                .time(projection.bid().getTime())
                .slug(projection.slug())
                .bidder(UserMapper.toSummaryDto(projection.userProjection()))
                .build();
    }

    public BidChartDto toChartDto(BidPointProjection projection) {
        return BidChartDto.builder()
                .amount(projection.amount())
                .time(projection.time())
                .user(projection.user())
                .build();
    }
}
