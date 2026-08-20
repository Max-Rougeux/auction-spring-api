package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.RefreshTokenDao;
import com.rougeux.auction.dal.sqlserver.mapper.RefreshRowMapper;
import com.rougeux.auction.domain.bo.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.Optional;

@Repository
@Profile("sqlserver")
@RequiredArgsConstructor
public class SqlRefreshDao implements RefreshTokenDao {

    private final NamedParameterJdbcTemplate template;

    @Override
    public Optional<RefreshToken> findByPublicId(String id) {
        return template.query("""
                                SELECT id, public_id, username, hash, created_at, expires_at
                                FROM REFRESH_TOKENS
                                WHERE public_id = :public_id
                                """,
                        new MapSqlParameterSource().addValue("public_id", id),
                        RefreshRowMapper::mapRefreshToken).stream()
                .findFirst();
    }

    @Override
    public void save(RefreshToken token) {
        template.update("""
                INSERT INTO REFRESH_TOKENS(id, public_id, username, hash, created_at, expires_at)
                VALUES (:id, :public_id, :username, :hash, :created_at, :expires_at)
                """, new MapSqlParameterSource()
                .addValue("id", token.getId())
                .addValue("public_id", token.getPublicId())
                .addValue("username", token.getUsername())
                .addValue("hash", token.getHash())
                .addValue("created_at", token.getCreatedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())
                .addValue("expires_at", token.getExpiresAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()));
    }

    @Override
    public void delete(String publicId) {
        template.update("DELETE FROM REFRESH_TOKENS WHERE public_id = :id",
                new MapSqlParameterSource().addValue("id", publicId));
    }
}
