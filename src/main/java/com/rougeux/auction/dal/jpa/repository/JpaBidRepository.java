package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.entity.BidEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaBidRepository extends JpaRepository<BidEntity, UUID> {

    @Query("SELECT b FROM BidEntity b ORDER BY b.time DESC")
    @EntityGraph(attributePaths = { "sale", "bidder", "bidder.thumbnail" })
    Page<BidEntity> findAllOrderByTimeDesc(Pageable pageable);

    @EntityGraph(attributePaths = { "sale", "bidder", "bidder.thumbnail" })
    Page<BidEntity> findBySale_SlugOrderByTimeDesc(String slug, Pageable pageable);

    @EntityGraph(attributePaths = { "sale", "bidder", "bidder.thumbnail" })
    List<BidEntity> findAllBySale_SlugOrderByTimeAsc(String slug);

    Optional<BidEntity> findTopBySale_SlugOrderByAmountDesc(String slug);

    long countBySale_Slug(String slug);
}
