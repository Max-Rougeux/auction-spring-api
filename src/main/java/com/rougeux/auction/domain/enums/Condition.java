package com.rougeux.auction.domain.enums;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Condition {
    NEW,
    LIKE_NEW,
    VERY_GOOD,
    GOOD,
    FAIR,
    FOR_PARTS;

    private static final List<Condition> VALUES =
            Collections.unmodifiableList(List.of(values()));

    public static Condition randomCondition()  {
        return VALUES.get(ThreadLocalRandom.current().nextInt(VALUES.size()));
    }
}
