package io.github.eng1g4;

import static java.lang.Math.floor;

import io.github.eng1g4.util.IntPacker;
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

    public void add(int x, int y) {
        obstacles.add(IntPacker.pack(x, y));
    }

    // scales placed obstacles to new dimensions
    public void resize(float newWidth, float newHeight) {
        HashSet<Double> newObstacles = new HashSet<>();
        float widthRatio = width /newWidth;
        float heightRatio = height /newHeight;
        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                int scaledX = (int) floor((float)x * widthRatio);
                int scaledY = (int) floor((float)y * heightRatio);
                double packedScaledCords = IntPacker.pack(scaledX, scaledY);
                if (obstacles.contains(packedScaledCords)) {
                    double packedObstacle = IntPacker.pack(x, y);
                    newObstacles.add(packedObstacle);
                }
            }
        }
        width = newWidth;
        height = newHeight;
        obstacles = newObstacles;
    }


    public boolean contains(int x, int y){
        return obstacles.contains(IntPacker.pack(x, y));
    }
}

