package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.Item;
import com.rougeux.auction.domain.enums.Condition;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class ItemRowMapper {

    @SuppressWarnings("unused")
    public Item mapItem(ResultSet rs, int rowNum) throws SQLException {
        return Item.builder()
                .id(rs.getString("item_id"))
                .model(rs.getString("model"))
                .description(rs.getString("description"))
                .condition(Condition.valueOf(rs.getString("condition")))
                .year(rs.getInt("year"))
                .isGem(rs.getBoolean("is_gem"))
                .imageId(rs.getString("image_id"))
                .brandId(rs.getString("brand_id"))
                .categoryId(rs.getString("category_id"))
                .userId(rs.getString("user_id"))
                .build();
    }
}
