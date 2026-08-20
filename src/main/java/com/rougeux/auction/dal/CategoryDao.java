package com.rougeux.auction.dal;

import com.rougeux.auction.domain.bo.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {

    List<Category> findAll();
    List<Category> findAllWithCount();

    Optional<Category> findById(String id);

    long count();
    void save(Category category);
}
