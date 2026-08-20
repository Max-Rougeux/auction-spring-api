package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.Category;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class CategoryRowMapper {

    @SuppressWarnings("unused")
    public Category mapCategory(ResultSet rs, int rowNum) throws SQLException {
        return Category.builder()
                .id(rs.getString("category_id"))
                .slug(rs.getString("category_slug"))
                .label(rs.getString("label"))
                .build();
    }

    @SuppressWarnings("unused")
    public Category mapCategoryCount(ResultSet rs, int rowNum) throws SQLException {
        return Category.builder()
                .id(rs.getString("category_id"))
                .slug(rs.getString("category_slug"))
                .label(rs.getString("label"))
                .count(rs.getLong("count"))
                .build();
    }
}
