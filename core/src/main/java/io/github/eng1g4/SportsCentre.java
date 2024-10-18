package io.github.eng1g4;

public class SportsCentre extends Building {

    // REQUIRES ATTRIBUTION:
    // https://www.freepik.com/free-vector/soccer-stadium-isometric-projection-icon_3685353.htm#fromView=search&page=1&position=1&uuid=ccdaf982-270e-4166-ac80-4a6b9d0436db
    private static final String TEXTURE_PATH = "sports_centre.png";

	public SportsCentre(int x, int y) {
		super(TEXTURE_PATH, 4, 4, x, y);
	}
}
