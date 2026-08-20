package com.rougeux.auction.web.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"filename", "width", "height"})
public record ImageDto (
    String filename,
    int width,
    int height
) {}
