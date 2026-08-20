package com.rougeux.auction.dal.sqlserver.mapper.projection;

import com.rougeux.auction.dal.sqlserver.mapper.BrandRowMapper;
import com.rougeux.auction.dal.sqlserver.mapper.CategoryRowMapper;
import com.rougeux.auction.dal.sqlserver.mapper.ItemRowMapper;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.projection.ItemProjection;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class ItemProjectionRowMapper {

    @SuppressWarnings("unused")
    public ItemProjection mapProjection(ResultSet rs, int rowNum) throws SQLException {
        return ItemProjection.builder()
                .item(ItemRowMapper.mapItem(rs, rowNum))
                .image(Image.builder()
                        .filename(rs.getString("filename"))
                        .width(rs.getInt("width"))
                        .height(rs.getInt("height"))
                        .build())
                .brand(BrandRowMapper.mapBrand(rs, rowNum))
                .category(CategoryRowMapper.mapCategory(rs, rowNum))
                .build();
    }
}
