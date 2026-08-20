package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.RefreshTokenDao;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.bo.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockRefreshDao implements RefreshTokenDao {

    @Override
    public Optional<RefreshToken> findByPublicId(String id) {
        return InMemoryStorage.REFRESH_TOKENS.stream()
                .filter(t -> id.equals(t.getPublicId())).findFirst();
    }

    @Override
    public void save(RefreshToken token) {
        InMemoryStorage.REFRESH_TOKENS.add(token);
    }

    @Override
    public void delete(String publicId) {
        InMemoryStorage.REFRESH_TOKENS
                .removeIf(t -> publicId.equals(t.getPublicId()));
    }
}
