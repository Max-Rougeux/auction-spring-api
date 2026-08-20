package com.rougeux.auction.dal.jpa.converter;

import com.rougeux.auction.domain.enums.State;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class StateConverter implements AttributeConverter<Set<State>, String> {

    @Override
    public String convertToDatabaseColumn(Set<State> states) {
        if (states == null || states.isEmpty()) return "{}";
        return "{" + states.stream()
                .map(State::name)
                .collect(Collectors.joining(",")) + "}";
    }

    @Override
    public Set<State> convertToEntityAttribute(String value) {
        if (value == null || value.isBlank()) return Set.of();
        return Arrays.stream(value
                        .replace("{", "")
                        .replace("}", "")
                        .split(","))
                .map(String::trim)
                .map(State::valueOf)
                .collect(Collectors.toSet());
    }
}