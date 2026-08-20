package com.rougeux.auction.dal.jpa.entity;

import com.rougeux.auction.domain.enums.Condition;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ITEMS")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ItemEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String model;
    private String description;
    private int year;
    private boolean isGem;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ImageEntity thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity owner;
}
