package com.rougeux.auction.service.action;

import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.domain.aggregate.BiddingAggregate;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.event.BidPlacedEvent;
import com.rougeux.auction.exception.NotFoundException;
import com.rougeux.auction.service.BidService;
import com.rougeux.auction.service.cache.SaleCache;
import com.rougeux.auction.service.security.CustomUserDetailsService;
import com.rougeux.auction.web.request.BidRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rougeux.auction.support.ApiCodes.CD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BiddingService {

    private final SaleCache saleCache;
    private final BidService bidService;
    private final SaleDao saleRepository;
    private final CreditService creditService;
    private final ApplicationEventPublisher publisher;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public void placeBid(@Valid BidRequest request, String subject) {
        Sale sale = saleRepository.findBySlugForUpdate(request.slug())
                .orElseThrow(() -> new NotFoundException(CD_NOT_FOUND, "sales.error.notFound"));

        BiddingAggregate agg = BiddingAggregate.builder()
                .saleProjection(saleCache.getProjectionBySlug(request.slug()))
                .bidder(userDetailsService.loadUserByUsername(subject).user())
                .bidProjection(bidService.getFirstBySale(request.slug()))
                .amount(request.amount())
                .build();
        agg.validate();

        bidService.save(agg.toBid());
        saleCache.persist(agg.updatedSale(sale));
        creditService.debit(agg.bidder(), agg.amount());

        BidProjection previousBid = agg.bidProjection();

        if (previousBid != null)
            creditService.refund(previousBid, request.amount());

        publisher.publishEvent(BidPlacedEvent.builder()
                .bid(bidService.getFirstBySale(request.slug()))
                .build());
    }
}
