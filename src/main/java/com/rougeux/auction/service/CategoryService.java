package com.rougeux.auction.service;

import com.rougeux.auction.dal.CategoryDao;
import com.rougeux.auction.mapper.CategoryMapper;
import com.rougeux.auction.web.category.CategoryCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao repository;

    @Cacheable(value = "categories")
    public List<CategoryCountDto> getAll() {
        return repository.findAllWithCount().stream()
                .map(CategoryMapper::toCountDto).toList();
    }
}
