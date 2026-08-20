package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.enums.State;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class SaleRowMapper {

    @SuppressWarnings("unused")
    public Sale mapSale(ResultSet rs, int rowNum) throws SQLException {
        return Sale.builder()
                .id(rs.getString("sale_id"))
                .slug(rs.getString("slug"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .startedAt(rs.getObject("started_at", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .endedAt(rs.getObject("ended_at", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .startingPrice(rs.getInt("starting_price"))
                .currentPrice(rs.getInt("current_price"))
                .likes(rs.getInt("likes"))
                .state(State.valueOf(rs.getString("state")))
                .itemId(rs.getString("item_id"))
                .build();
    }
}
