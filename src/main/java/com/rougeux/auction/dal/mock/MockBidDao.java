package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.BidDao;
import com.rougeux.auction.dal.mock.loader.BidDataLoader;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.domain.bo.Bid;
import com.rougeux.auction.dal.mock.batch.BidBatchDao;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.exception.NotFoundException;
import com.rougeux.auction.support.ApiCodes;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockBidDao implements BidDao, BidBatchDao {

    private final BidDataLoader loader;

    @Override
    public List<Bid> findAll() {
        return InMemoryStorage.BIDS;
    }

    @Override
    public List<BidProjection> findAllProjectedBySale(int page, int limit, String slug) {

        Sale sale = InMemoryStorage.SALES.stream()
                .filter(s -> slug.equals(s.getSlug()))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(ApiCodes.CD_NOT_FOUND, "sales.error.notFound"));

        List<Bid> saleBids = InMemoryStorage.BIDS.stream()
                .filter(b -> sale.getId().equals(b.getSaleId()))
                .sorted(Comparator.comparing(Bid::getTime, Comparator.reverseOrder()))
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .toList();

        return saleBids.isEmpty() ? List.of() : loader.load(saleBids);
    }

    @Override
    public List<BidProjection> findLatestProjected(int limit) {
        List<Bid> bids = InMemoryStorage.BIDS.stream()
                .sorted(Comparator.comparing(Bid::getTime, Comparator.reverseOrder()))
                .limit(limit)
                .toList();

        return loader.load(bids);
    }

    @Override
    public List<BidPointProjection> findAllPointsBySale(String slug) {
        Sale sale = InMemoryStorage.SALES.stream()
                .filter(s -> slug.equals(s.getSlug()))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(ApiCodes.CD_NOT_FOUND, "sales.error.notFound"));

        List<Bid> saleBids = InMemoryStorage.BIDS.stream()
                .filter(b -> sale.getId().equals(b.getSaleId()))
                .sorted(Comparator.comparing(Bid::getTime))
                .toList();

        return saleBids.isEmpty() ? List.of() : loader.loadPoints(saleBids);
    }

    @Override
    public Optional<BidProjection> findFirstBySale(String slug) {
        Sale sale = InMemoryStorage.SALES.stream()
                .filter(s -> slug.equals(s.getSlug()))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(ApiCodes.CD_NOT_FOUND, "sales.error.notFound"));

        List<Bid> saleBids = InMemoryStorage.BIDS.stream()
                .filter(b -> sale.getId().equals(b.getSaleId()))
                .sorted(Comparator.comparing(Bid::getAmount, Comparator.reverseOrder()))
                .limit(1)
                .toList();

        return loader.load(saleBids).stream().findFirst();
    }

    @Override
    public long count(@Nullable String slug) {
        if (slug == null)
            return InMemoryStorage.BIDS.size();

        return InMemoryStorage.SALES.stream()
                .filter(s -> slug.equals(s.getSlug()))
                .findFirst()
                .map(sale -> InMemoryStorage.BIDS.stream()
                        .filter(b -> sale.getId().equals(b.getSaleId()))
                        .count())
                .orElse(0L);
    }

    @Override
    public void save(Bid bid) {
        for (int i = 0; i < InMemoryStorage.BIDS.size(); i++) {
            if (Objects.equals(InMemoryStorage.BIDS.get(i).getId(), bid.getId())) {
                InMemoryStorage.BIDS.set(i, bid);
                return;
            }
        }
        InMemoryStorage.BIDS.add(bid);
    }

    @Override
    public Map<String, Bid> findAllByIds(Set<String> ids) {
        return InMemoryStorage.BIDS.stream()
                .filter(i -> ids.contains(i.getId()))
                .collect(toMap(Bid::getId, identity()));
    }
}
