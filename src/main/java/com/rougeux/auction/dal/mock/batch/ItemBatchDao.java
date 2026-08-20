package com.rougeux.auction.dal.mock.batch;

import com.rougeux.auction.domain.bo.Item;

import java.util.Map;
import java.util.Set;

public interface ItemBatchDao {
    Map<String, Item> findAllByIds(Set<String> ids);
}