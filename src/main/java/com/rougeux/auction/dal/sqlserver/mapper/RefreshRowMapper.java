package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.RefreshToken;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class RefreshRowMapper {

    @SuppressWarnings("unused")
    public static RefreshToken mapRefreshToken(ResultSet rs, int rowNum) throws SQLException {
        return RefreshToken.builder()
                .id(rs.getString("id"))
                .publicId(rs.getString("public_id"))
                .username(rs.getString("username"))
                .hash(rs.getString("hash"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .expiresAt(rs.getObject("expires_at", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .build();
    }
}
