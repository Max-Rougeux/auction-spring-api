package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface JpaRefreshRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByPublicId(String id);

    @Modifying
    @Transactional
    void deleteByPublicId(String id);
}
