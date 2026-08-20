package com.rougeux.auction.service.cache;

import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.projection.SaleProjection;
import com.rougeux.auction.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rougeux.auction.support.ApiCodes.CD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SaleCache {

    private final SaleDao repository;

    @Cacheable(value = "sales", key = "#page + '-' + #category")
    public List<SaleProjection> getAll(int page, int limit, String category) {
        return repository.findAllProjected(page, limit, category);
    }

    @Cacheable(value = "sales", key = "#slug")
    public SaleProjection getProjectionBySlug(String slug) {
        return repository.findProjectedBySlug(slug)
                .orElseThrow(() -> new NotFoundException(CD_NOT_FOUND, "sales.error.notFound"));
    }

    @CacheEvict(value = "sales", allEntries = true)
    public void persist(Sale sale) {
        repository.save(sale);
    }
}
