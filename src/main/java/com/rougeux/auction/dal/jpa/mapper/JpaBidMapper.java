package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.BidEntity;
import com.rougeux.auction.dal.jpa.entity.SaleEntity;
import com.rougeux.auction.dal.jpa.entity.UserEntity;
import com.rougeux.auction.domain.bo.Bid;
import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaBidMapper {

    public static BidEntity from(Bid bo, UserEntity user, SaleEntity sale) {
        return BidEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .amount(bo.getAmount())
                .time(bo.getTime())
                .bidder(user)
                .sale(sale)
                .build();
    }

    public static Bid toBo(BidEntity entity) {
        return Bid.builder()
                .id(String.valueOf(entity.getId()))
                .amount(entity.getAmount())
                .time(entity.getTime())
                .userId(String.valueOf(entity.getBidder().getId()))
                .saleId(String.valueOf(entity.getSale().getId()))
                .build();
    }

    public static BidProjection toProjection(BidEntity entity) {
        return BidProjection.builder()
                .bid(toBo(entity))
                .slug(entity.getSale().getSlug())
                .userProjection(JpaUserMapper.toProjection(entity.getBidder()))
                .build();
    }

    public static BidPointProjection toPointProjection(BidEntity entity) {
        return BidPointProjection.builder()
                .amount(entity.getAmount())
                .time(entity.getTime())
                .user(entity.getBidder().getFirstname() + ' ' + entity.getBidder().getLastname())
                .build();
    }
}
