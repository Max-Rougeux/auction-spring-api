package com.rougeux.auction.dal.jpa.converter;

import com.rougeux.auction.domain.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RolesConverter implements AttributeConverter<Set<Role>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) return "{}";
        return "{" + roles.stream()
                .map(Role::name)
                .collect(Collectors.joining(",")) + "}";
    }

    @Override
    public Set<Role> convertToEntityAttribute(String value) {
        if (value == null || value.isBlank()) return Set.of();
        return Arrays.stream(value
                        .replace("{", "")
                        .replace("}", "")
                        .split(","))
                .map(String::trim)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}