package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {


    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findBySlug(String slug);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    @EntityGraph(attributePaths = { "thumbnail" })
    Optional<UserEntity> findDetailsByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.credit = :credit WHERE u.id = :id")
    void updateCredit(@Param("id") UUID id, @Param("credit") int credit);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.credit = credit - :amount WHERE u.id = :id")
    void debitCredit(@Param("id") UUID id, @Param("amount") int amount);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.credit = credit + :amount WHERE u.id = :id")
    void refundCredit(@Param("id") UUID id, @Param("amount") int amount);
}
