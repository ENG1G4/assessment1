package io.github.eng1g4.building.impl;

import io.github.eng1g4.building.Building;
import io.github.eng1g4.building.BuildingType;

public class Restaurant extends Building {

    // REQUIRES ATTRIBUTION
    // https://www.freepik.com/free-vector/restaurant-cut-view_1304799.htm#fromView=search&page=1&position=2&uuid=8af6ec5a-f2c1-49d4-ad93-fedb4a77aab5
    private static final String TEXTURE_PATH = "restaurant.png";

    public Restaurant(int x, int y) {
        super(TEXTURE_PATH, 4, 5, x, y, BuildingType.RESTAURANT);
    }

}
