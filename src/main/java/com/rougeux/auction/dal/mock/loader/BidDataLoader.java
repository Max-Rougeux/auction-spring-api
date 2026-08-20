package com.rougeux.auction.dal.mock.loader;

import com.rougeux.auction.dal.mock.batch.*;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.domain.projection.UserProjection;
import com.rougeux.auction.domain.bo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.*;

@Component
@Profile("mock")
@RequiredArgsConstructor
public class BidDataLoader {

    private final ImageBatchDao imageBatch;
    private final UserBatchDao userBatch;

    public List<BidProjection> load(List<Bid> bids) {

        Set<String> biddersId = bids.stream()
                .map(Bid::getUserId)
                .collect(toSet());
        Map<String, User> users = userBatch.findAllByIds(biddersId);

        Set<String> userImageIds = users.values().stream()
                .map(User::getImageId)
                .collect(toSet());
        Map<String, Image> images = imageBatch.findAllByIds(userImageIds);

        Set<String> saleIds = bids.stream().map(Bid::getSaleId).collect(toSet());
        Map<String, Sale> sales = InMemoryStorage.SALES.stream()
                .filter(s -> saleIds.contains(s.getId()))
                .collect(toMap(Sale::getId, identity()));


        return bids.stream()
                .sorted(Comparator.comparing(Bid::getAmount, Comparator.reverseOrder()))
                .map(bid -> {
                    User user = users.get(bid.getUserId());
                    Sale sale = sales.get(bid.getSaleId());

                    return BidProjection.builder()
                            .bid(bid)
                            .slug(sale.getSlug())
                            .userProjection(UserProjection.builder()
                                    .user(user)
                                    .image(images.get(user.getImageId()))
                                    .build())
                            .build();
                })
                .toList();
    }

    public List<BidPointProjection> loadPoints(List<Bid> bids) {
        Set<String> userIds = bids.stream()
                .map(Bid::getUserId)
                .collect(toSet());

        Map<String, User> users = userBatch.findAllByIds(userIds);

        return bids.stream()
                .sorted(Comparator.comparing(Bid::getTime))
                .map(bid -> {
                    User user = users.get(bid.getUserId());

                    return BidPointProjection.builder()
                            .amount(bid.getAmount())
                            .time(bid.getTime())
                            .user(user.getSlug())
                            .build();
                    }).toList();
    }
}
