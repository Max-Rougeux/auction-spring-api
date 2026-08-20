package com.rougeux.auction.domain.bo;

import com.rougeux.auction.domain.enums.Condition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String model;
    private String description;

    private Condition condition;
    private int year;
    private boolean isGem;

    private String userId;
    private String imageId;
    private String brandId;
    private String categoryId;
}

