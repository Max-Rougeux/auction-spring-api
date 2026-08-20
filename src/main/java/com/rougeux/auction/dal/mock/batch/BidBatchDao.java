package com.rougeux.auction.dal.mock.batch;

import com.rougeux.auction.domain.bo.Bid;

import java.util.Map;
import java.util.Set;

public interface BidBatchDao {
    Map<String, Bid> findAllByIds(Set<String> ids);
}