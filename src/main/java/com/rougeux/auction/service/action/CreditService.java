package com.rougeux.auction.service.action;

import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.event.CreditUpdateEvent;
import com.rougeux.auction.service.cache.MeCache;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final MeCache cache;
    private final UserDao repository;
    private final ApplicationEventPublisher publisher;

    public void debit(User user, int amount) {
        repository.debit(user.getId(), amount);

        cache.evict(user.getUsername());
        publisher.publishEvent(CreditUpdateEvent.builder()
                .amount(amount)
                .username(user.getUsername())
                .refund(false)
                .build());
    }

    public void refund(@NonNull BidProjection projection, int outbid) {
        User user = projection.userProjection().user();
        repository.refund(user.getId(), projection.bid().getAmount());

        cache.evict(user.getUsername());
        publisher.publishEvent(CreditUpdateEvent.builder()
                .amount(projection.bid().getAmount())
                .username(user.getUsername())
                .slug(projection.slug())
                .outbid(outbid)
                .refund(true)
                .build());
    }
}
