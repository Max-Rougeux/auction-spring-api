package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.dal.mock.batch.SaleBatchDao;
import com.rougeux.auction.dal.mock.loader.SaleDataLoader;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.projection.SaleProjection;
import com.rougeux.auction.domain.bo.Sale;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockSaleDao implements SaleDao, SaleBatchDao {

    private final SaleDataLoader loader;

    @Override
    public List<Sale> findAll() {
        return InMemoryStorage.SALES;
    }

    @Override
    public Optional<Sale> findBySlugForUpdate(String slug) {
        return InMemoryStorage.SALES.stream()
                .filter(sale -> slug.equals(sale.getSlug()))
                .findFirst();
    }

    @Override
    public List<SaleProjection> findAllProjected(int page, int limit, @Nullable String category) {
        return loader.load(InMemoryStorage.SALES)
                .stream()
                .filter(agg -> category == null || category.equals(agg.itemProjection().category().getSlug()))
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .toList();
    }

    @Override
    public Optional<SaleProjection> findProjectedBySlug(String slug) {
        return loader.load(InMemoryStorage.SALES).stream()
                .filter(agg -> slug.equals(agg.sale().getSlug()))
                .findFirst();
    }

    @Override
    public long count(@Nullable String category) {
        if(category == null)
            return InMemoryStorage.SALES.size();

        return loader.load(InMemoryStorage.SALES).stream()
                .filter(p -> category.equals(p.itemProjection().category().getSlug()))
                .count();
    }

    @Override
    public void save(Sale sale) {
        for (int i = 0; i < InMemoryStorage.SALES.size(); i++) {
            if (Objects.equals(InMemoryStorage.SALES.get(i).getId(), sale.getId())) {
                InMemoryStorage.SALES.set(i, sale);
                return;
            }
        }
        InMemoryStorage.SALES.add(sale);
    }

    @Override
    public Map<String, Sale> findAllByIds(Set<String> ids) {
        return InMemoryStorage.SALES.stream()
                .filter(i -> ids.contains(i.getId()))
                .collect(toMap(Sale::getId, identity()));
    }
}
