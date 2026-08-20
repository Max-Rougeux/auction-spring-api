package com.rougeux.auction.mapper;

import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.web.image.ImageDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageMapper {

    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .filename(image.getFilename())
                .width(image.getWidth())
                .height(image.getHeight())
                .build();
    }
}
