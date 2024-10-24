package io.github.eng1g4.building.impl;

import io.github.eng1g4.building.Building;
import io.github.eng1g4.building.BuildingType;

public class SportsCentre extends Building {

    private static final String TEXTURE_PATH = "sports_centre.png";

	public SportsCentre(int x, int y) {
		super(TEXTURE_PATH, 4, 4, x, y, BuildingType.SPORTS_CENTRE);
	}
}
