package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.BidDao;
import com.rougeux.auction.dal.jpa.entity.SaleEntity;
import com.rougeux.auction.dal.jpa.entity.UserEntity;
import com.rougeux.auction.dal.jpa.mapper.JpaBidMapper;
import com.rougeux.auction.dal.jpa.repository.JpaBidRepository;
import com.rougeux.auction.domain.bo.Bid;
import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaBidDao implements BidDao {

    private final EntityManager manager;
    private final JpaBidRepository repository;

    @Override
    public List<Bid> findAll() {
        return repository.findAll().stream()
                .map(JpaBidMapper::toBo)
                .toList();
    }

    @Override
    public List<BidProjection> findAllProjectedBySale(int page, int limit, String slug) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "time"));

        return repository.findBySale_SlugOrderByTimeDesc(slug, pageable).getContent().stream()
                .map(JpaBidMapper::toProjection)
                .toList();
    }

    @Override
    public List<BidProjection> findLatestProjected(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return repository.findAllOrderByTimeDesc(pageable).getContent().stream()
                .map(JpaBidMapper::toProjection)
                .toList();
    }

    @Override
    public List<BidPointProjection> findAllPointsBySale(String slug) {
        return repository.findAllBySale_SlugOrderByTimeAsc(slug).stream()
                .map(JpaBidMapper::toPointProjection)
                .toList();
    }

    @Override
    public Optional<BidProjection> findFirstBySale(String slug) {
        return repository.findTopBySale_SlugOrderByAmountDesc(slug)
                .map(JpaBidMapper::toProjection);
    }

    @Override
    public long count(@Nullable String sale) {
        return sale != null ? repository.countBySale_Slug(sale) : repository.count();
    }

    @Override
    public void save(Bid bid) {
        repository.save(JpaBidMapper.from(bid,
                manager.getReference(UserEntity.class, UUID.fromString(bid.getUserId())),
                manager.getReference(SaleEntity.class, UUID.fromString(bid.getSaleId()))));
    }
}
