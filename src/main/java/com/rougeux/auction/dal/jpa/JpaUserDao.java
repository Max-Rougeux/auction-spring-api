package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.dal.jpa.entity.ImageEntity;
import com.rougeux.auction.dal.jpa.mapper.JpaUserMapper;
import com.rougeux.auction.dal.jpa.repository.JpaUserRepository;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.projection.UserProjection;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaUserDao implements UserDao {

    private final EntityManager manager;
    private final JpaUserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(JpaUserMapper::toBo)
                .toList();
    }

    @Override
    public List<UserProjection> findAllProjected(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        return repository.findAll(pageable).getContent().stream()
                .map(JpaUserMapper::toProjection)
                .toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(UUID.fromString(id))
                .map(JpaUserMapper::toBo);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(JpaUserMapper::toBo);
    }

    @Override
    public Optional<UserProjection> findProjectedByUsername(String username) {
        return repository.findDetailsByUsername(username)
                .map(JpaUserMapper::toProjection);
    }

    @Override
    public Optional<User> findBySlug(String slug) {
        return repository.findBySlug(slug).map(JpaUserMapper::toBo);
    }

    @Override
    public Optional<UserProjection> findProjectedBySlug(String slug) {
        return repository.findBySlug(slug)
                .map(JpaUserMapper::toProjection);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void save(User user) {
        repository.save(JpaUserMapper.from(user,
                manager.getReference(ImageEntity.class, UUID.fromString(user.getImageId()))));
    }

    @Override
    public void updateCredit(String id, int credit) {
        repository.updateCredit(UUID.fromString(id), credit);
    }

    @Override
    public void debit(String id, int amount) {
        repository.debitCredit(UUID.fromString(id), amount);
    }

    @Override
    public void refund(String id, int amount) {
        repository.refundCredit(UUID.fromString(id), amount);
    }
}
