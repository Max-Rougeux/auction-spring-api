package com.rougeux.auction.dal.sqlserver.mapper;

import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.enums.ImageType;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class ImageRowMapper {

    @SuppressWarnings("unused")
    public Image mapImage(ResultSet rs, int rowNum) throws SQLException {
        return Image.builder()
                .id(rs.getString("image_id"))
                .filename(rs.getString("filename"))
                .directory(rs.getString("directory"))
                .width(rs.getInt("width"))
                .height(rs.getInt("height"))
                .type(ImageType.valueOf(rs.getString("type")))
                .uploadAt(rs.getObject("upload_at", LocalDateTime.class)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .build();
    }
}
