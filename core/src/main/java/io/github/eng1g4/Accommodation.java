package io.github.eng1g4;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

import static java.lang.Float.max;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class Accomodation extends  {

    public Accomodation(String texturePath, int width, int height, int x, int y,ArrayList<ArrayList<Float>> studentDensityMap ) {
        super(texturePath, width, height, x, y, Color.ORANGE);
    }
}
