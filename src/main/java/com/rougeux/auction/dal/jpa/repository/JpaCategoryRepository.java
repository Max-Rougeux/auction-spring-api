package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.contract.CategoryCount;
import com.rougeux.auction.dal.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    @Query("""
    SELECT c as category, COUNT(i.id) as count
    FROM CategoryEntity c
    LEFT JOIN ItemEntity i ON i.category.id = c.id
    GROUP BY c
    ORDER BY c.label ASC
    """)
    List<CategoryCount> findAllWithCount();
}
