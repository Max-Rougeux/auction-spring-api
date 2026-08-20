package com.rougeux.auction.web.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Meta(int page, int size, long total, int pages) {}