package com.rougeux.auction.dal;

import com.rougeux.auction.domain.bo.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandDao {

    List<Brand> findAll();

    Optional<Brand> findById(String id);

    long count();
    void save(Brand brand);
}
