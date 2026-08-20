package com.rougeux.auction.dal.sqlserver.mapper.projection;

import com.rougeux.auction.dal.sqlserver.mapper.UserRowMapper;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.projection.UserProjection;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class UserProjectionRowMapper {

    public UserProjection mapProjection(ResultSet rs, int rowNum) throws SQLException {
        return UserProjection.builder()
                .user(UserRowMapper.mapUser(rs, rowNum))
                .image(Image.builder()
                        .filename(rs.getString("filename"))
                        .width(rs.getInt("width"))
                        .height(rs.getInt("height"))
                        .build())
                .build();
    }
}
