package io.github.eng1g4.building;

public class Accommodation extends Building {

    // ATTRIBUTION REQUIRED:
    // https://www.freepik.com/free-vector/isometric-view-modern-office-building_3290417.htm#fromView=search&page=1&position=2&uuid=fb869e6f-9d00-48f9-aec5-e36a75e22a29
    private static final String TEXTURE_PATH = "accommodation.png";

    public Accommodation(int x, int y) {
        super(TEXTURE_PATH, 4, 4, x, y, BuildingType.ACCOMMODATION);
    }
}
