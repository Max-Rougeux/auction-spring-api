package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.CategoryDao;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.bo.Category;
import com.rougeux.auction.dal.mock.batch.CategoryBatchDao;
import com.rougeux.auction.domain.bo.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockCategoryDao implements CategoryDao, CategoryBatchDao {

    @Override
    public List<Category> findAll() {
        return InMemoryStorage.CATEGORIES;
    }

    @Override
    public List<Category> findAllWithCount() {
        Map<String, Long> countByCategory = InMemoryStorage.ITEMS.stream()
                .collect(Collectors.groupingBy(Item::getCategoryId, Collectors.counting()));

        List<Category> list = new ArrayList<>();
        for (Category c : InMemoryStorage.CATEGORIES) {
            c.setCount(countByCategory.getOrDefault(c.getId(), 0L));
            if (c.getCount() > 0) {
                list.add(c);
            }
        }
        return list;
    }

    @Override
    public Optional<Category> findById(String id) {
        return InMemoryStorage.CATEGORIES.stream()
                .filter(c -> id.equals(c.getId()))
                .findFirst();
    }

    @Override
    public long count() {
        return InMemoryStorage.CATEGORIES.size();
    }

    @Override
    public void save(Category category) {
        for (int i = 0; i < InMemoryStorage.CATEGORIES.size(); i++) {
            if (Objects.equals(InMemoryStorage.CATEGORIES.get(i).getId(), category.getId())) {
                InMemoryStorage.CATEGORIES.set(i, category);
                return;
            }
        }
        InMemoryStorage.CATEGORIES.add(category);
    }

    @Override
    public Map<String, Category> findAllByIds(Set<String> ids) {
        return InMemoryStorage.CATEGORIES.stream()
                .filter(i -> ids.contains(i.getId()))
                .collect(toMap(Category::getId, identity()));
    }
}
