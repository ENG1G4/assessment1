package io.github.eng1g4.building;

public class BuildingManager {

    private final int[] buildingCount = new int[BuildingType.COUNT];

    public int getBuildingCount(BuildingType buildingType) {
        return this.buildingCount[buildingType.ordinal()];
    }

    public void registerBuilding(BuildingType buildingType) {
        this.buildingCount[buildingType.ordinal()] += 1;
    }

}
