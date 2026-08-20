package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.RefreshTokenDao;
import com.rougeux.auction.dal.jpa.mapper.JpaRefreshMapper;
import com.rougeux.auction.dal.jpa.repository.JpaRefreshRepository;
import com.rougeux.auction.domain.bo.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaRefreshDao implements RefreshTokenDao {

    private final JpaRefreshRepository repository;

    @Override
    public Optional<RefreshToken> findByPublicId(String id) {
        return repository.findByPublicId(id).map(JpaRefreshMapper::toBo);
    }

    @Override
    public void save(RefreshToken token) {
        repository.save(JpaRefreshMapper.from(token));
    }

    @Override
    public void delete(String publicId) {
        repository.deleteByPublicId(publicId);
    }
}
