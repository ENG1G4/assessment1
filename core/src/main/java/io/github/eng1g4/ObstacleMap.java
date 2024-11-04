package io.github.eng1g4;

import static java.lang.Math.floor;

import java.util.HashSet;

public class ObstacleMap {

    private float height;
    private float width;
    private HashSet<Double> obstacles;

    public ObstacleMap(float width, float height) {
        this.width = width;
        this.height = height;
        obstacles = new HashSet<>();
    }

    public void add(int x, int y){
        obstacles.add(new IntTuple(x, y).pack());
    }

    public void resize(float newWidth, float newHeight){
        HashSet<Double> newObstacles = new HashSet<>();
        float widthRatio = width /newWidth;
        float heightRatio = height /newHeight;
        for (int x = 0; x < newWidth; x++){
            for (int y = 0; y < newHeight; y++){

                int p0 = (int) floor((float)x * widthRatio);
                int p1 = (int) floor((float)y * heightRatio);
                IntTuple tuple = new IntTuple(p0, p1);
                double packedValue = tuple.pack();
                if (obstacles.contains(packedValue)){
                    IntTuple newObstacle = new IntTuple(x, y);
                    double packedObstacle = newObstacle.pack();
                    newObstacles.add(packedObstacle);
                }
            }
        }
        width = newWidth;
        height = newHeight;
        obstacles = newObstacles;
    }


    public boolean contains(int x, int y){
        return obstacles.contains(new IntTuple(x, y).pack());
    }
}

