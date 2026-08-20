package com.rougeux.auction.dal;

import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.projection.UserProjection;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAll();
    List<UserProjection> findAllProjected(int page, int limit);

    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    Optional<UserProjection> findProjectedByUsername(String username);

    Optional<User> findBySlug(String slug);
    Optional<UserProjection> findProjectedBySlug(String slug);

    long count();
    void save(User user);
    void updateCredit(String id, int credit);
    void debit(String id, int amount);
    void refund(String id, int amount);
}
