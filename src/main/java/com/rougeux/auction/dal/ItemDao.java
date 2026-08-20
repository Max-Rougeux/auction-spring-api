package com.rougeux.auction.dal;

import com.rougeux.auction.domain.bo.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {

    List<Item> findAll();
    Optional<Item> findById(String id);

    long count();
    void save(Item item);
}
