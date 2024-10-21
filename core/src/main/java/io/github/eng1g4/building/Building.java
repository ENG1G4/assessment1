package io.github.eng1g4.building;

public abstract class Building extends PlaceableObject implements IBuilding {

    private final BuildingType buildingType;

    public Building(String texturePath, int width, int height, int x, int y, BuildingType buildingType) {
        super(texturePath, width, height, x, y);

        this.buildingType = buildingType;
    }

    public BuildingType getBuildingType() {
        return this.buildingType;
    }

    @Override
    public String getName() {
        return this.buildingType.getName();
    }
}
