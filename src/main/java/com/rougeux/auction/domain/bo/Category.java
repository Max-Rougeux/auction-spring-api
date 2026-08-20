package com.rougeux.auction.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String slug;
    private String label;

    private Long count;
}
