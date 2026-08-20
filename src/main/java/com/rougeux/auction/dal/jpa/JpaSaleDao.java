package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.dal.jpa.entity.ItemEntity;
import com.rougeux.auction.dal.jpa.mapper.JpaSaleMapper;
import com.rougeux.auction.dal.jpa.repository.JpaSaleRepository;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.projection.SaleProjection;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaSaleDao implements SaleDao {

    private final EntityManager manager;
    private final JpaSaleRepository repository;

    @Override
    public List<Sale> findAll() {
        return repository.findAll().stream()
                .map(JpaSaleMapper::toBo)
                .toList();
    }

    @Override
    public Optional<Sale> findBySlugForUpdate(String slug) {
        return repository.findBySlugForUpdate(slug).map(JpaSaleMapper::toBo);
    }

    @Override
    public List<SaleProjection> findAllProjected(int page, int limit, @Nullable String category) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return repository.findAllWithDetails(category, pageable).getContent().stream()
                .map(JpaSaleMapper::toProjection)
                .toList();
    }

    @Override
    public Optional<SaleProjection> findProjectedBySlug(String slug) {
        return repository.findBySlug(slug)
                .map(JpaSaleMapper::toProjection);
    }

    @Override
    public long count(@Nullable String category) {
        return repository.countByCategory(category);
    }

    @Override
    public void save(Sale sale) {
        repository.save(JpaSaleMapper.from(sale,
                manager.getReference(ItemEntity.class, UUID.fromString(sale.getItemId()))));
    }
}
