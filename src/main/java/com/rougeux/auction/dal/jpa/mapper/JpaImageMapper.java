package com.rougeux.auction.dal.jpa.mapper;

import com.rougeux.auction.dal.jpa.entity.ImageEntity;
import com.rougeux.auction.domain.bo.Image;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class JpaImageMapper {

    public static ImageEntity from(Image bo) {
        return ImageEntity.builder()
                .id(UUID.fromString(bo.getId()))
                .filename(bo.getFilename())
                .directory(bo.getDirectory())
                .width(bo.getWidth())
                .height(bo.getHeight())
                .uploadAt(bo.getUploadAt())
                .type(bo.getType())
                .build();
    }

    public static Image toBo(ImageEntity entity) {
        return Image.builder()
                .id(String.valueOf(entity.getId()))
                .filename(entity.getFilename())
                .directory(entity.getDirectory())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .uploadAt(entity.getUploadAt())
                .build();
    }
}
