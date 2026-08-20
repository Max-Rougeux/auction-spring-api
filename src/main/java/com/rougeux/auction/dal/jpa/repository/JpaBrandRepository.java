package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaBrandRepository extends JpaRepository<BrandEntity, UUID> {
}
