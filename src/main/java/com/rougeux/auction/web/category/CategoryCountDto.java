package com.rougeux.auction.web.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"slug", "label"})
public record CategoryCountDto(
    String slug,
    String label,
    long count
) {}
