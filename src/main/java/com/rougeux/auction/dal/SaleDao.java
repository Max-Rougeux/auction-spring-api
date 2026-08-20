package com.rougeux.auction.dal;

import com.rougeux.auction.domain.projection.SaleProjection;
import com.rougeux.auction.domain.bo.Sale;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public interface SaleDao {

    List<Sale> findAll();
    Optional<Sale> findBySlugForUpdate(String slug);

    List<SaleProjection> findAllProjected(int page, int limit, @Nullable String category);
    Optional<SaleProjection> findProjectedBySlug(String slug);

    long count(@Nullable String category);
    void save(Sale sale);
}
