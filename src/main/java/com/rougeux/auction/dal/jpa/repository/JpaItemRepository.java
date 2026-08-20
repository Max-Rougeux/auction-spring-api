package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaItemRepository extends JpaRepository<ItemEntity, UUID> {
}
