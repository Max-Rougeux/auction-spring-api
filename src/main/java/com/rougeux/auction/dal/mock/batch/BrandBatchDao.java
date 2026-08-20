package com.rougeux.auction.dal.mock.batch;

import com.rougeux.auction.domain.bo.Brand;

import java.util.Map;
import java.util.Set;

public interface BrandBatchDao {
    Map<String, Brand> findAllByIds(Set<String> ids);
}