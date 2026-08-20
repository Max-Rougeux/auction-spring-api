package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.dal.mock.loader.UserDataLoader;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.dal.mock.batch.UserBatchDao;
import com.rougeux.auction.domain.projection.UserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockUserDao implements UserDao, UserBatchDao {

    private final UserDataLoader loader;

    @Override
    public List<User> findAll() {
        return InMemoryStorage.USERS;
    }

    @Override
    public List<UserProjection> findAllProjected(int page, int limit) {
        return loader.load(InMemoryStorage.USERS).stream()
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .toList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return InMemoryStorage.USERS.stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst();
    }

    @Override
    public Optional<UserProjection> findProjectedByUsername(String username) {
        return loader.load(InMemoryStorage.USERS).stream()
                .filter(p -> username.equals(p.user().getUsername()))
                .findFirst();
    }

    @Override
    public Optional<User> findBySlug(String slug) {
        return InMemoryStorage.USERS.stream()
                .filter(u -> slug.equals(u.getSlug()))
                .findFirst();
    }

    @Override
    public Optional<UserProjection> findProjectedBySlug(String slug) {
        return loader.load(InMemoryStorage.USERS).stream()
                .filter(p -> slug.equals(p.user().getSlug()))
                .findFirst();
    }

    @Override
    public Optional<User> findById(String id) {
        return InMemoryStorage.USERS.stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst();
    }

    @Override
    public long count() {
        return InMemoryStorage.USERS.size();
    }

    @Override
    public void save(User user) {
        for (int i = 0; i < InMemoryStorage.USERS.size(); i++) {
            if (Objects.equals(InMemoryStorage.USERS.get(i).getId(), user.getId())) {
                InMemoryStorage.USERS.set(i, user);
                return;
            }
        }
        InMemoryStorage.USERS.add(user);
    }

    @Override
    public void updateCredit(String id, int credit) {
        InMemoryStorage.USERS.stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .ifPresent(u -> u.setCredit(credit));
    }

    @Override
    public void debit(String id, int amount) {
        InMemoryStorage.USERS.stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .ifPresent(u -> u.setCredit(u.getCredit() - amount));
    }

    @Override
    public void refund(String id, int amount) {
        InMemoryStorage.USERS.stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .ifPresent(u -> u.setCredit(u.getCredit() + amount));
    }

    @Override
    public Map<String, User> findAllByIds(Set<String> ids) {
        return InMemoryStorage.USERS.stream()
                .filter(i -> ids.contains(i.getId()))
                .collect(toMap(User::getId, identity()));
    }
}
