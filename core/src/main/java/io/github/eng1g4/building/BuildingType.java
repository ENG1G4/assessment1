package io.github.eng1g4.building;

import java.util.Arrays;
import java.util.List;

public enum BuildingType {

    ACCOMMODATION("Accommodation", 4, 4, 1.78f, 0, 1f),
    SPORTS_CENTRE("Sports Centre", 4, 4, 1.18f, 0, 1f),
    LECTURE_THEATRE("Lecture Theatre", 4, 5, 1.2f, 0.17f, 2.1f),
    RESTAURANT("Restaurant", 4, 4, 1.5f, 0, 1f),

    // Use this constant to get the number of buildings. Its ordinal will always be the number of
    // constants above it.
    COUNT("", 0, 0, 0, 0 ,0);

    // Calling values() repeatedly is expensive.
    // Copies values() into an unmodifiable list, cutting off COUNT.
    public static final List<BuildingType> cachedValues = List.of(Arrays.copyOf(BuildingType.values(), BuildingType.COUNT.ordinal()));
    private final String name;
    private final int width;
    private final int height;
    private final float textureHeight;
    private final float textureXoffset;
    private final float textureYoffset;

    BuildingType(String name, int width, int height, float textureHeight, float textureXOffset, float textureYOffset) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.textureHeight = textureHeight;
        this.textureXoffset = textureXOffset;
        this.textureYoffset = textureYOffset;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getTextureHeight(){
        return textureHeight;
    }

    public float getXOffset(){
        return textureXoffset;
    }

    public float getYOffset(){
        return textureYoffset;
    }

    public String getName() {
        return name;
    }
}
