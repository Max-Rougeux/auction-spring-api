package com.rougeux.auction.domain.bo;

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
public class Brand {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String name;
    private Instant partnerSince;
}
