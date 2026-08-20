package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.ItemDao;
import com.rougeux.auction.dal.jpa.entity.BrandEntity;
import com.rougeux.auction.dal.jpa.entity.CategoryEntity;
import com.rougeux.auction.dal.jpa.entity.ImageEntity;
import com.rougeux.auction.dal.jpa.entity.UserEntity;
import com.rougeux.auction.dal.jpa.mapper.JpaItemMapper;
import com.rougeux.auction.dal.jpa.repository.JpaItemRepository;
import com.rougeux.auction.domain.bo.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaItemDao implements ItemDao {

    private final EntityManager manager;
    private final JpaItemRepository repository;

    @Override
    public List<Item> findAll() {
        return repository.findAll().stream()
                .map(JpaItemMapper::toBo)
                .toList();
    }

    @Override
    public Optional<Item> findById(String id) {
        return repository.findById(UUID.fromString(id))
                .map(JpaItemMapper::toBo);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void save(Item item) {
        repository.save(JpaItemMapper.from(item,
                manager.getReference(ImageEntity.class,     UUID.fromString(item.getImageId())),
                manager.getReference(BrandEntity.class,     UUID.fromString(item.getBrandId())),
                manager.getReference(CategoryEntity.class,  UUID.fromString(item.getCategoryId())),
                manager.getReference(UserEntity.class,      UUID.fromString(item.getUserId()))));
    }
}
