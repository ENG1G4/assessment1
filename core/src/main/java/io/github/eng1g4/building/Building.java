package io.github.eng1g4.building;

public abstract class Building extends PlaceableObject implements IBuilding {

    private final BuildingType buildingType;

    public Building(String texturePath, int x, int y, BuildingType buildingType) {
        super(texturePath, buildingType.getWidth(), buildingType.getHeight(), x, y);

        this.buildingType = buildingType;
    }

    public BuildingType getBuildingType() {
        return this.buildingType;
    }

    @Override
    public float getTextureHeight(){
        return buildingType.getTextureHeight();
    }

    @Override
    public float getXOffset(){
        return buildingType.getXOffset();
    }

    @Override
    public float getYOffset(){
        return buildingType.getYOffset();
    }

    @Override
    public String getName() {
        return this.buildingType.getName();
    }
}
