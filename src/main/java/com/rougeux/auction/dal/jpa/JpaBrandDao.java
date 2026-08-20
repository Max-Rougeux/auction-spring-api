package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.BrandDao;
import com.rougeux.auction.dal.jpa.mapper.JpaBrandMapper;
import com.rougeux.auction.dal.jpa.repository.JpaBrandRepository;
import com.rougeux.auction.domain.bo.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaBrandDao implements BrandDao {

    private final JpaBrandRepository repository;

    @Override
    public List<Brand> findAll() {
        return repository.findAll().stream()
                .map(JpaBrandMapper::toBo)
                .toList();
    }

    @Override
    public Optional<Brand> findById(String id) {
        return repository.findById(UUID.fromString(id)).map(JpaBrandMapper::toBo);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void save(Brand brand) {
        repository.save(JpaBrandMapper.from(brand));
    }
}
