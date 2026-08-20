package com.rougeux.auction.domain.aggregate;

import com.rougeux.auction.domain.bo.Bid;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.enums.State;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.domain.projection.SaleProjection;
import com.rougeux.auction.exception.BusinessException;
import com.rougeux.auction.support.ApiCodes;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.Instant;

@Builder
public record BiddingAggregate(SaleProjection saleProjection, User bidder, @Nullable BidProjection bidProjection, int amount) {
    public void validate() {
        if (saleProjection.sale().getState() != State.OPEN)
            throw new BusinessException(ApiCodes.CD_ERR_BID_SALE_CLOSED, "bid.error.sale.closed");

        if (saleProjection.itemProjection().item().getUserId().equals(bidder.getId()))
            throw new BusinessException(ApiCodes.CD_ERR_BID_SALE_OWNER, "bid.error.owner.submit");

        if (bidProjection != null && bidder.getId().equals(bidProjection.userProjection().user().getId()))
            throw new BusinessException(ApiCodes.CD_ERR_BID_ALREADY_TOP, "bid.error.alreadyTopBidder");

        if (bidProjection != null ? amount <= bidProjection.bid().getAmount() : amount <= saleProjection.sale().getStartingPrice())
            throw new BusinessException(ApiCodes.CD_ERR_BID_AMOUNT_LOW, "bid.error.insufficientAmount");

        if (bidder.getCredit() < amount)
            throw new BusinessException(ApiCodes.CD_ERR_BID_CREDIT_LOW, "bid.error.insufficientCredit");

    }

    public Bid toBid() {
        return Bid.builder()
                .amount(amount)
                .time(Instant.now())
                .saleId(saleProjection.sale().getId())
                .userId(bidder.getId())
                .build();
    }

    public Sale updatedSale(Sale sale) {
        return sale.toBuilder()
                .currentPrice(amount)
                .build();
    }
}
