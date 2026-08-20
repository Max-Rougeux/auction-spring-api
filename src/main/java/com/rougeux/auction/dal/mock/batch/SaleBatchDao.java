package com.rougeux.auction.dal.mock.batch;

import com.rougeux.auction.domain.bo.Sale;

import java.util.Map;
import java.util.Set;

public interface SaleBatchDao {
    Map<String, Sale> findAllByIds(Set<String> ids);
}