package com.rougeux.auction.dal;

import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.domain.bo.Bid;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public interface BidDao {

    List<Bid> findAll();
    List<BidProjection> findAllProjectedBySale(int page, int limit, String slug);
    List<BidProjection> findLatestProjected(int limit);

    List<BidPointProjection> findAllPointsBySale(String slug);

    Optional<BidProjection> findFirstBySale(String slug);

    long count(@Nullable String sale);
    void save(Bid bid);
}
