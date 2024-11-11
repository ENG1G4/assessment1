package io.github.eng1g4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import io.github.eng1g4.building.BuildingManager;
import io.github.eng1g4.building.BuildingType;
import io.github.eng1g4.building.PlaceableObject;
import io.github.eng1g4.building.impl.Accommodation;
import io.github.eng1g4.building.impl.LectureTheatre;
import io.github.eng1g4.building.impl.Restaurant;
import io.github.eng1g4.building.impl.SportsCentre;
import java.util.ArrayList;

public class Map implements Disposable {
    private final int width;
    private final int height;
    private final Texture backgroundTexture;
    private float originX;
    private float originY;
    private float tileWidth;
    private float tileHeight;

    private final float virtualWidth;
    private final float virtualHeight;
    private final ArrayList<PlaceableObject> placeableObjects;

    private BuildingType selectedBuilding = BuildingType.ACCOMMODATION;
    private final BuildingManager buildingManager;
    private ObstacleMap obstacleMap;
    private SoundManager soundManager;

    public Map
        (String backgroundTexturePath, int width, int height,
        float virtualWidth, float virtualHeight, BuildingManager buildingManager,
         SoundManager soundManager)
    {
        this.width = width;
        this.height = height;
        this.buildingManager = buildingManager;
        this.soundManager = soundManager;

        backgroundTexture = new Texture(Gdx.files.internal(backgroundTexturePath));

        placeableObjects = new ArrayList<>();

        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;

        originX = 0;
        originY = 0;

        setTileSizeAndOrigin();

        createObstacleMap();
    }

    private void createObstacleMap() {
        int width = 20;
        int height = 20;
        obstacleMap = new ObstacleMap(width, height);

        // river
        for (int x = 0; x < height; x++) {
            int y = height / 2 + (int)(Math.sin(x * 0.5) * 2);
            obstacleMap.add(x, y);
            obstacleMap.add(x, y + 1);
        }

        // rocks
        obstacleMap.add(3, 5);
        obstacleMap.add(4, 5);
        obstacleMap.add(5, 6);

        obstacleMap.add(10, 4);
        obstacleMap.add(10, 5);

        obstacleMap.add(15, 13);
        obstacleMap.add(16, 13);
        obstacleMap.add(16, 12);

        obstacleMap.add(18, 14);
        obstacleMap.add(19, 14);

        obstacleMap.add(2, 3);
        obstacleMap.add(3, 3);
        obstacleMap.add(4, 4);

        obstacleMap.add(7, 7);
        obstacleMap.add(8, 7);
        obstacleMap.add(8, 8);

        obstacleMap.add(12, 2);
        obstacleMap.add(13, 2);
        obstacleMap.add(14, 3);

        obstacleMap.add(6, 15);
        obstacleMap.add(7, 15);
        obstacleMap.add(7, 16);

        obstacleMap.add(11, 17);
        obstacleMap.add(12, 17);

        obstacleMap.add(15, 18);
        obstacleMap.add(16, 18);
        obstacleMap.add(16, 19);

        obstacleMap.add(17, 6);
        obstacleMap.add(18, 6);
        obstacleMap.add(18, 7);

        obstacleMap.resize(this.width, this.height);

    }
    private void drawGrid(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1); // White color for lines

        // Precompute the center positions of all diamonds
        float[][] centerX = new float[width + 1][height + 1];
        float[][] centerY = new float[width + 1][height + 1];
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                centerX[x][y] = originX + (x - y) * (tileWidth / 2);
                centerY[x][y] = originY + (x + y) * (tileHeight / 2);
            }
        }

        // Draw the grid lines
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                float cx = centerX[x][y];
                float cy = centerY[x][y];

                // Draw line to the right neighbor
                if (x < width) {
                    float neighborX = centerX[x + 1][y];
                    float neighborY = centerY[x + 1][y];
                    shapeRenderer.line(cx, cy, neighborX, neighborY);
                }

                // Draw line to the bottom neighbor
                if (y < height) {
                    float neighborX = centerX[x][y + 1];
                    float neighborY = centerY[x][y + 1];
                    shapeRenderer.line(cx, cy, neighborX, neighborY);
                }
            }
        }

        shapeRenderer.end();
    }

    private void colourCell(ShapeRenderer shapeRenderer, int tileX, int tileY, Color color){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);

        float[] screenCoordinates = tileToScreen(tileX, tileY);
        float screenX = screenCoordinates[0] - tileWidth / 2f;
        float screenY = screenCoordinates[1];

        // Calculate the four corners of the diamond (tile)
        float y0 = screenY + (tileHeight / 2f);

        float x1 = screenX + (tileWidth / 2f);
        float y1 = screenY + tileHeight;

        float x2 = screenX + tileWidth;
        float y2 = screenY + (tileHeight / 2f);

        float x3 = screenX + (tileWidth / 2f);

        shapeRenderer.triangle(screenX, y0, x1, y1, x2, y2);
        shapeRenderer.triangle(x2, y2, x3, screenY, screenX, y0);

        shapeRenderer.end();
    }

    private void drawHoveredCell(ShapeRenderer shapeRenderer, float mouseWorldX, float mouseWorldY) {
        // Determine which tile the mouse is over
        int[] hoveredTile = screenToTile(mouseWorldX, mouseWorldY);
        int tileX = hoveredTile[0];
        int tileY = hoveredTile[1];

        // First, draw the highlighted tile if it is within the map bounds
        if (tileX >= 0 && tileX < width && tileY >= 0 && tileY < height) {
            colourCell(shapeRenderer, tileX, tileY, Color.BLUE);
        }
    }

    public void handleClickLeft(float worldX, float worldY) {
        // Convert world coordinates to tile coordinates
        int[] tileCoords = screenToTile(worldX, worldY);
        int tileX = tileCoords[0];
        int tileY = tileCoords[1];

        // Check if the tile is within the map bounds
        if (! (tileX >= 0 && tileX < width && tileY >= 0 && tileY < height)) {
            return;
        }

        int buildingWidth = selectedBuilding.getWidth();
        int buildingHeight = selectedBuilding.getHeight();

        // Check if buildings are not present when making a new one
        Rectangle newBuildingRectangle = new Rectangle(tileX, tileY, buildingWidth, buildingHeight);
        for (PlaceableObject placeableObject : this.placeableObjects) {
            if (placeableObject.overlaps(newBuildingRectangle)) {
                return;
            }
        }

        // Check out-of-bounds placement
        if ((tileX - buildingWidth + 1) < 0 || (tileY - buildingHeight + 1) < 0 ) {
            return;
        }

        // Check obstacle collision
        for (int x = tileX; x > tileX - buildingWidth; x--) {
            for (int y = tileY; y > tileY - buildingHeight; y--) {
                if (obstacleMap.contains(x, y)){
                    return;

                }
            }
        }


        // Create new building
        switch (selectedBuilding) {
            case ACCOMMODATION:
                placeableObjects.add(new Accommodation(tileX, tileY));
                break;
            case SPORTS_CENTRE:
                placeableObjects.add(new SportsCentre(tileX, tileY));
                break;
            case LECTURE_THEATRE:
                placeableObjects.add(new LectureTheatre(tileX, tileY));
                break;
            case RESTAURANT:
                placeableObjects.add(new Restaurant(tileX, tileY));
                break;
            default:
                System.out.println("NO building for that YET");
                return;
        }

        soundManager.playBuildingPlacementSound();
        buildingManager.registerBuilding(selectedBuilding);

    }


    private void drawObstacles(ShapeRenderer shapeRenderer) {
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                if (obstacleMap.contains(x, y)){
                    colourCell(shapeRenderer, x, y, Color.RED);
                }
            }
        }


    }

    private void drawBuildings(SpriteBatch batch) {
        batch.begin();

        for (PlaceableObject building: placeableObjects){
            float [] buildingCoordinates = tileToScreen((int)building.getX(), (int)building.getY());

            building.draw(
                batch, buildingCoordinates[0], buildingCoordinates[1],
                this.tileWidth, this.tileHeight);
        }

        batch.end();
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float mouseWorldX, float mouseWorldY) {
        drawGrid(shapeRenderer);
        drawHoveredCell(shapeRenderer, mouseWorldX, mouseWorldY);
        drawObstacles(shapeRenderer);
        drawBuildings(batch);
    }


    private int[] screenToTile(float worldX, float worldY) {
        // https://clintbellanger.net/articles/isometric_math/
        // the link above justifies all the formulas used


        // Adjust coordinates relative to origin
        float sX = worldX - originX;
        float sY = worldY - originY;

        float halfTileWidth = tileWidth / 2f;
        float halfTileHeight = tileHeight / 2f;

        // Calculate transformed coordinates
        float isoX = (sY / halfTileHeight + sX / halfTileWidth) / 2f;
        float isoY = (sY / halfTileHeight - sX / halfTileWidth) / 2f;

        int tileX = (int)Math.floor(isoX);
        int tileY = (int)Math.floor(isoY);

        // Check if the coordinates are within the map bounds
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return new int[] { -1, -1 };
        } else {
            return new int[] { tileX, tileY };
        }
    }

    private float[] tileToScreen(int tileX, int tileY) {
        // https://clintbellanger.net/articles/isometric_math/
        // the link above justifies all the formulas used

        float halfTileWidth = tileWidth / 2f;
        float halfTileHeight = tileHeight / 2f;

        // Calculate transformed coordinates
        float sX = halfTileWidth * (tileX - tileY);
        float sY = halfTileHeight * (tileX + tileY);

        // Adjust coordinates relative to origin
        float worldX = sX + originX;
        float worldY = sY + originY;

        return new float[] { worldX, worldY };
    }

    public void setTileSizeAndOrigin() {

        tileWidth = 2f * virtualWidth / (width + height);
        tileHeight = tileWidth / 2f; // Ensuring isometric tiles

        // Calculate total grid dimensions
        float totalGridWidth = ((width + height - 2) * (tileWidth / 2f)) + tileWidth;
        float totalGridHeight = ((width + height - 2) * (tileHeight / 2f)) + tileHeight;

        originX = (virtualWidth - totalGridWidth) / 2f + virtualWidth/2f;
        originY = (virtualHeight - totalGridHeight) / 2f;
    }

    public void selectBuilding(BuildingType buildingType) {
        selectedBuilding = buildingType;
        System.out.println("Building " + buildingType + " selected");
    }

    public BuildingType getSelectedBuilding(){
        return selectedBuilding;
    }

    public void dispose() {
        this.backgroundTexture.dispose();
        this.placeableObjects.forEach(PlaceableObject::dispose);
    }
}
