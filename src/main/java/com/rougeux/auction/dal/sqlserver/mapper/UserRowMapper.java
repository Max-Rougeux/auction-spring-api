package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.enums.Role;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class UserRowMapper {

    @SuppressWarnings("unused")
    public User mapUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getString("user_id"))
                .slug(rs.getString("slug"))
                .username(rs.getString("username"))
                .firstname(rs.getString("firstname"))
                .lastname(rs.getString("lastname"))
                .phone(rs.getString("phone"))
                .credit(rs.getInt("credit"))
                .roles(Arrays.stream(rs.getString("roles").split(","))
                        .map(Role::valueOf)
                        .collect(Collectors.toSet()))
                .enabled(rs.getBoolean("enabled"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .imageId(rs.getString("image_id"))
                .build();
    }

    @SuppressWarnings("unused")
    public User mapPrincipal(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getString("user_id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .slug(rs.getString("slug"))
                .credit(rs.getInt("credit"))
                .roles(Arrays.stream(rs.getString("roles").split(","))
                        .map(Role::valueOf)
                        .collect(Collectors.toSet()))
                .enabled(rs.getBoolean("enabled"))
                .build();
    }
}
