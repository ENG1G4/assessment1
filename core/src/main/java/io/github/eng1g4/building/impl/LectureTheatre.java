package io.github.eng1g4.building.impl;

import io.github.eng1g4.building.Building;
import io.github.eng1g4.building.BuildingType;

public class LectureTheatre extends Building {

    private static final String TEXTURE_PATH = "lecture_theatre_cropped.png";

    public LectureTheatre(int x, int y) {
        super(TEXTURE_PATH, BuildingType.LECTURE_THEATRE.getWidth(), BuildingType.LECTURE_THEATRE.getHeight(),
            x, y, BuildingType.LECTURE_THEATRE);
    }
}
