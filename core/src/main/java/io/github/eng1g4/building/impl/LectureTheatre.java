package io.github.eng1g4.building.impl;

import io.github.eng1g4.building.Building;
import io.github.eng1g4.building.BuildingType;

public class LectureTheatre extends Building {

    // REQUIRES ATTRIBUTION
    // https://www.freepik.com/free-vector/isometric-cinema-composition_5973013.htm#fromView=search&page=1&position=15&uuid=425cc2e6-9053-4654-a7ff-f76715058685
    private static final String TEXTURE_PATH = "lecture_theatre.png";

    public LectureTheatre(int x, int y) {
        super(TEXTURE_PATH, 4, 4, x, y, BuildingType.LECTURE_THEATRE);
    }
}
