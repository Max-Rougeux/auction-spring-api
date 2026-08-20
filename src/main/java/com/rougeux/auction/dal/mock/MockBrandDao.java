package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.BrandDao;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.bo.Brand;
import com.rougeux.auction.dal.mock.batch.BrandBatchDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockBrandDao implements BrandDao, BrandBatchDao {

    @Override
    public List<Brand> findAll() {
        return InMemoryStorage.BRANDS;
    }

    @Override
    public Optional<Brand> findById(String id) {
        return InMemoryStorage.BRANDS.stream()
                .filter(brand -> id.equals(brand.getId()))
                .findFirst();
    }

    @Override
    public long count() {
        return InMemoryStorage.BRANDS.size();
    }

    @Override
    public void save(Brand brand) {
        for (int i = 0; i < InMemoryStorage.BRANDS.size(); i++) {
            if (Objects.equals(InMemoryStorage.BRANDS.get(i).getId(), brand.getId())) {
                InMemoryStorage.BRANDS.set(i, brand);
                return;
            }
        }
        InMemoryStorage.BRANDS.add(brand);
    }

    @Override
    public Map<String, Brand> findAllByIds(Set<String> ids) {
        return InMemoryStorage.BRANDS.stream()
                .filter(i -> ids.contains(i.getId()))
                .collect(toMap(Brand::getId, identity()));
    }
}
