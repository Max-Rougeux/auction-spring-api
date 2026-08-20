package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.Bid;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class BidRowMapper {

    @SuppressWarnings("unused")
    public Bid mapBid(ResultSet rs, int rowNum) throws SQLException {
        return Bid.builder()
                .id(rs.getString("bid_id"))
                .amount(rs.getInt("amount"))
                .time(rs.getObject("time", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .saleId(rs.getString("sale_id"))
                .userId(rs.getString("user_id"))
                .build();
    }
}
