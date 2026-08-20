package com.rougeux.auction.web.api;

import lombok.Builder;

@Builder
public record Slice<T>(T data, Meta meta) {}

