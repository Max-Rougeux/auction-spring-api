package com.rougeux.auction.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class SlugUtils {

    public String slugify(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "-")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("(^-)|(−$)", "") + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
