package io.github.eng1g4.building;

import java.util.Arrays;
import java.util.List;

public enum BuildingType {

    ACCOMMODATION("Accommodation"),
    SPORTS_CENTRE("Sports Centre"),

    // Use this constant to get the number of buildings. Its ordinal will always be the number of
    // constants above it.
    COUNT("");

    // Calling values() repeatedly is expensive.
    // Copies values() into an unmodifiable list, cutting off COUNT.
    public static final List<BuildingType> cachedValues = List.of(Arrays.copyOf(BuildingType.values(), BuildingType.COUNT.ordinal()));
    private final String name;

    BuildingType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
