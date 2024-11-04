package io.github.eng1g4.building.impl;

import io.github.eng1g4.building.Building;
import io.github.eng1g4.building.BuildingType;

public class SportsCentre extends Building {

    private static final String TEXTURE_PATH = "sports_centre_cropped.png";

	public SportsCentre(int x, int y) {
		super(TEXTURE_PATH, x, y, BuildingType.SPORTS_CENTRE);
	}
}
