package com.rougeux.auction.service.cache;

import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.domain.projection.UserProjection;
import com.rougeux.auction.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.rougeux.auction.support.ApiCodes.CD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MeCache {

    private final UserDao repository;

    @Cacheable(value = "me", key = "#subject")
    public UserProjection getProfile(String subject) {
        return repository.findProjectedByUsername(subject)
                .orElseThrow(() -> new NotFoundException(CD_NOT_FOUND, "users.error.notFound"));
    }

    @CacheEvict(value = "me", key = "#subject")
    public void evict(String subject) {
        // evict cache data
    }
}
