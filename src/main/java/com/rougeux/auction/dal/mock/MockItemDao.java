package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.ItemDao;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.bo.Item;
import com.rougeux.auction.dal.mock.batch.ItemBatchDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockItemDao implements ItemDao, ItemBatchDao {

    @Override
    public List<Item> findAll() {
        return InMemoryStorage.ITEMS;
    }

    @Override
    public Optional<Item> findById(String id) {
        return InMemoryStorage.ITEMS.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();
    }

    @Override
    public long count() {
        return InMemoryStorage.ITEMS.size();
    }

    @Override
    public void save(Item item) {
        for (int i = 0; i < InMemoryStorage.ITEMS.size(); i++) {
            if (Objects.equals(InMemoryStorage.ITEMS.get(i).getId(), item.getId())) {
                InMemoryStorage.ITEMS.set(i, item);
                return;
            }
        }
        InMemoryStorage.ITEMS.add(item);
    }

    @Override
    public Map<String, Item> findAllByIds(Set<String> ids) {
        return InMemoryStorage.ITEMS.stream()
                .filter(i -> ids.contains(i.getId()))
                .collect(toMap(Item::getId, identity()));
    }
}
