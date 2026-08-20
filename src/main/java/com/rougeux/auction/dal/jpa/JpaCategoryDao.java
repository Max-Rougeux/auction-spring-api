package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.CategoryDao;
import com.rougeux.auction.dal.jpa.mapper.JpaCategoryMapper;
import com.rougeux.auction.dal.jpa.repository.JpaCategoryRepository;
import com.rougeux.auction.domain.bo.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaCategoryDao implements CategoryDao {

    private final JpaCategoryRepository repository;

    @Override
    public List<Category> findAll() {
        return repository.findAll().stream()
                .map(JpaCategoryMapper::toBo)
                .toList();
    }

    @Override
    public List<Category> findAllWithCount() {
        return repository.findAllWithCount().stream()
                .map(cc -> {
                    Category bo = JpaCategoryMapper.toBo(cc.getCategory());
                    bo.setCount(cc.getCount());
                    return bo;
                })
                .toList();
    }

    @Override
    public Optional<Category> findById(String id) {
        return repository.findById(UUID.fromString(id)).map(JpaCategoryMapper::toBo);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void save(Category category) {
        repository.save(JpaCategoryMapper.from(category));
    }
}
