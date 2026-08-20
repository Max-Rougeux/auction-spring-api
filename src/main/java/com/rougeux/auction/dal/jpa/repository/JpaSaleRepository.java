package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.entity.SaleEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaSaleRepository extends JpaRepository<SaleEntity, UUID>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SaleEntity s WHERE s.slug = :slug")
    Optional<SaleEntity> findBySlugForUpdate(@Param("slug") String slug);

    @EntityGraph(attributePaths = {
            "item", "item.thumbnail", "item.brand",
            "item.category", "item.owner", "item.owner.thumbnail"
    })
    Optional<SaleEntity> findBySlug(String slug);


    @Query("""
    SELECT s FROM SaleEntity s
    JOIN FETCH s.item i
    JOIN FETCH i.thumbnail
    JOIN FETCH i.brand
    JOIN FETCH i.category c
    JOIN FETCH i.owner o
    LEFT JOIN FETCH o.thumbnail
    WHERE (:category IS NULL OR c.slug = :category)
    ORDER BY s.endedAt DESC
    """)
    Page<SaleEntity> findAllWithDetails(@Param("category") String category, Pageable pageable);

    @Query("SELECT COUNT(s) FROM SaleEntity s JOIN s.item i JOIN i.category c WHERE (:category IS NULL OR c.slug = :category)")
    long countByCategory(@Param("category") String category);
}
