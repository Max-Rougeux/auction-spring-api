package com.rougeux.auction.dal.sqlserver.mapper.projection;

import com.rougeux.auction.dal.sqlserver.mapper.SaleRowMapper;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.projection.SaleProjection;
import com.rougeux.auction.domain.projection.UserProjection;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class SaleProjectionRowMapper {

    @SuppressWarnings("unused")
    public SaleProjection mapProjection(ResultSet rs, int rowNum) throws SQLException {
        return SaleProjection.builder()
                .sale(SaleRowMapper.mapSale(rs, rowNum))
                .itemProjection(ItemProjectionRowMapper.mapProjection(rs, rowNum))
                .userProjection(UserProjection.builder()
                        .user(User.builder()
                                .id(rs.getString("user_id"))
                                .slug(rs.getString("user_slug"))
                                .firstname(rs.getString("firstname"))
                                .lastname(rs.getString("lastname"))
                                .createdAt(rs.getObject("user_created_at", LocalDateTime.class)
                                        .atZone(ZoneId.systemDefault()).toInstant())
                                .build())
                        .image(Image.builder()
                                .filename(rs.getString("user_filename"))
                                .width(rs.getInt("user_width"))
                                .height(rs.getInt("user_height"))
                                .build())
                        .build())
                .build();
    }
}
