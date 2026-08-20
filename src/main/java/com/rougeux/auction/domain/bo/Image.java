package com.rougeux.auction.domain.bo;

import com.rougeux.auction.domain.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String filename;
    private String directory;
    private int width;
    private int height;
    private ImageType type;
    private Instant uploadAt;
}
