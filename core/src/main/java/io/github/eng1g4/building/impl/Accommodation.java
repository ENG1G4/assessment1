package io.github.eng1g4.building.impl;

import io.github.eng1g4.building.Building;
import io.github.eng1g4.building.BuildingType;

public class Accommodation extends Building {

    private static final String TEXTURE_PATH = "accommodation_cropped.png";

    public Accommodation(int x, int y) {
        super(TEXTURE_PATH, BuildingType.ACCOMMODATION.getWidth(), BuildingType.ACCOMMODATION.getHeight(),
            x, y, BuildingType.ACCOMMODATION);
    }
}
