package com.rougeux.auction.dal.jpa.entity;

import com.rougeux.auction.domain.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "IMAGES")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ImageEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String filename;
    private String directory;
    private int width;
    private int height;
    private Instant uploadAt;

    @Enumerated(EnumType.STRING)
    private ImageType type;
}
