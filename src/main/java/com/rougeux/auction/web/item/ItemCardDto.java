package com.rougeux.auction.web.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rougeux.auction.web.category.CategoryDto;
import com.rougeux.auction.web.image.ImageDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"brand", "model", "gem", "category", "thumbnail"})
public record ItemCardDto (
    String brand,
    String model,
    boolean isGem,
    CategoryDto category,
    ImageDto thumbnail
) {}
