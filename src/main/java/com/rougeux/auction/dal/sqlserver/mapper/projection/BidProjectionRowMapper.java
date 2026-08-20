package com.rougeux.auction.dal.sqlserver.mapper.projection;

import com.rougeux.auction.dal.sqlserver.mapper.BidRowMapper;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.domain.projection.UserProjection;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class BidProjectionRowMapper {

    @SuppressWarnings("unused")
    public BidProjection mapProjection(ResultSet rs, int rowNum) throws SQLException {
        return BidProjection.builder()
                .bid(BidRowMapper.mapBid(rs, rowNum))
                .slug(rs.getString("sale_slug"))
                .userProjection(UserProjection.builder()
                        .user(User.builder()
                                .id(rs.getString("user_id"))
                                .slug(rs.getString("user_slug"))
                                .username(rs.getString("username"))
                                .firstname(rs.getString("firstname"))
                                .lastname(rs.getString("lastname"))
                                .credit(rs.getInt("credit"))
                                .createdAt(rs.getObject("created_at", LocalDateTime.class)
                                        .atZone(ZoneId.systemDefault()).toInstant())
                                .build())
                        .image(Image.builder()
                                .filename(rs.getString("filename"))
                                .width(rs.getInt("width"))
                                .height(rs.getInt("height"))
                                .build())
                        .build())
                .build();

    }

    @SuppressWarnings("unused")
    public BidPointProjection mapPointProjection(ResultSet rs, int rowNum) throws SQLException {
        String user = rs.getString("firstname") + ' ' + rs.getString("lastname");

        return BidPointProjection.builder()
                .amount(rs.getInt("amount"))
                .time(rs.getObject("time", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .user(user)
                .build();
    }
}
