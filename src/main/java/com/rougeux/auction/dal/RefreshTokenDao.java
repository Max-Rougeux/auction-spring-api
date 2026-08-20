package com.rougeux.auction.dal;

import com.rougeux.auction.domain.bo.RefreshToken;

import java.util.Optional;

public interface RefreshTokenDao {

    Optional<RefreshToken> findByPublicId(String id);

    void save(RefreshToken token);
    void delete(String publicId);
}
