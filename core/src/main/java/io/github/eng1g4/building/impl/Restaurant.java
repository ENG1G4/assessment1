package io.github.eng1g4.building.impl;

import io.github.eng1g4.building.Building;
import io.github.eng1g4.building.BuildingType;

public class Restaurant extends Building {

    private static final String TEXTURE_PATH = "restaurant.png";

    public Restaurant(int x, int y) {
        super(TEXTURE_PATH, 4, 5, x, y, BuildingType.RESTAURANT);
    }

}
