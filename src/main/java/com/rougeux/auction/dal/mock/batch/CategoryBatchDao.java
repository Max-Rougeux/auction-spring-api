package com.rougeux.auction.dal.mock.batch;

import com.rougeux.auction.domain.bo.Category;

import java.util.Map;
import java.util.Set;

public interface CategoryBatchDao {
    Map<String, Category> findAllByIds(Set<String> ids);
}