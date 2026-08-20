package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.Brand;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class BrandRowMapper {

    @SuppressWarnings("unused")
    public Brand mapBrand(ResultSet rs, int rowNum) throws SQLException {
        return Brand.builder()
                .id(rs.getString("brand_id"))
                .name(rs.getString("name"))
                .build();
    }
}
