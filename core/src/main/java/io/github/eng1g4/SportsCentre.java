package io.github.eng1g4;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import static java.lang.Float.max;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class SportsCentre extends PlacableObject {
	public SportsCentre(String texturePath, int width, int height, int x, int y, ArrayList<ArrayList<Float>> satisfactionMap) {
		super(texturePath, width, height, x, y, Color.CYAN);
		updateSatisfactionScore(satisfactionMap);
	}

	private float studentDensityFormula(float distance){
        float minimumDistance = 3 + max(this.getHeight(), this.getWidth());
        float squish = 0.1f;
        float adjustedDistance = max(distance - minimumDistance, 0);
        float distanceMul = 3;
        float peak = -10;
        return  max((float) (-pow(adjustedDistance, 2) * squish + distanceMul * adjustedDistance + peak), 0);
    }

	private void updateSatisfactionScore(ArrayList<ArrayList<Float>> satisfactionMap){
        for (int y = 0; y < satisfactionMap.size(); y++){
            for (int x = 0; x < satisfactionMap.get(y).size(); x++){
                float distanceX = abs(x - getTileX());
                float distanceY = abs(y - getTileY());

                float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                float initialDensity = satisfactionMap.get(y).get(x);
                
                satisfactionMap.get(y).set(x, initialDensity + studentDensityFormula(distance));
            }
        }
    }	
}
