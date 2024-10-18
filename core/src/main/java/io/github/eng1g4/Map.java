package io.github.eng1g4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Map extends ApplicationAdapter implements Disposable {
    private final int width;
    private final int height;
    private final Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private float originX;
    private float originY;
    private float tileWidth;
    private float tileHeight;

    private float virtualWidth;
    private float virtualHeight;
    private PlaceableObject objectToPlace;
    private final ArrayList<PlaceableObject> placeableObjects;

    private int selectedBuildingIndex;

    private int[] buildingCount;

    public Map(String backgroundTexturePath, int width, int height, float virtualWidth, float virtualHeight) {
        this.width = width;
        this.height = height;
        backgroundTexture = new Texture(Gdx.files.internal(backgroundTexturePath));

        placeableObjects = new ArrayList<>();

        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        tileWidth = virtualWidth / width;
        tileHeight = tileWidth / 2f;

        originX = 0;
        originY = 0;

        buildingCount = new int[5];


        setTileSizeAndOrigin();
    }

    private void drawOnGrid(Consumer<Diamond> consumer, int x, int y) {
        float screenX = (x - y) * (this.tileWidth / 2f) + this.originX;
        float screenY = (x + y) * (this.tileHeight / 2f) + this.originY;

        // Calculate the four corners of the diamond (tile)
        float x0 = screenX;
        float y0 = screenY + (tileHeight / 2f);

        float x1 = screenX + (tileWidth / 2f);
        float y1 = screenY + tileHeight;

        float x2 = screenX + tileWidth;
        float y2 = screenY + (tileHeight / 2f);

        float x3 = screenX + (tileWidth / 2f);
        float y3 = screenY;

        consumer.accept(new Diamond(x0, y0, x1, y1, x2, y2, x3, y3));
    }

    private void drawGrid(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1); // White color for lines

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                drawOnGrid(diamond -> {
                    //TODO too many lines are drawn if you think about it

                    // Draw lines between the points
                    shapeRenderer.line(diamond.x0(), diamond.y0(), diamond.x1(), diamond.y1());
                    shapeRenderer.line(diamond.x1(), diamond.y1(), diamond.x2(), diamond.y2());
                    shapeRenderer.line(diamond.x2(), diamond.y2(), diamond.x3(), diamond.y3());
                    shapeRenderer.line(diamond.x3(), diamond.y3(), diamond.x0(), diamond.y0());
                }, x, y);
            }
        }

        shapeRenderer.end();
    }

    private void colourCell(ShapeRenderer shapeRenderer, int tileX, int tileY, Color color){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);

        drawOnGrid(diamond -> {
            // Draw two triangles to fill the diamond
            shapeRenderer.triangle(diamond.x0(), diamond.y0(), diamond.x1(), diamond.y1(), diamond.x2(), diamond.y2());
            shapeRenderer.triangle(diamond.x2(), diamond.y2(), diamond.x3(), diamond.y3(), diamond.x0(), diamond.y0());
        }, tileX, tileY);

        shapeRenderer.end();
    }

    private void drawHoweveredCell(ShapeRenderer shapeRenderer, float mouseWorldX, float mouseWorldY) {
        // Determine which tile the mouse is over
        int[] hoveredTile = screenToTile(mouseWorldX, mouseWorldY);
        int hoverX = hoveredTile[0];
        int hoverY = hoveredTile[1];

        // First, draw the highlighted tile if it is within the map bounds
        if (hoverX >= 0 && hoverX < width && hoverY >= 0 && hoverY < height) {
            colourCell(shapeRenderer, hoverX, hoverY, Color.BLUE);
        }
    }

    public void handleClickLeft(float worldX, float worldY) {
        // Convert world coordinates to tile coordinates
        int[] tileCoords = screenToTile(worldX, worldY);
        int tileX = tileCoords[0];
        int tileY = tileCoords[1];

        // Check if the tile is within the map bounds
        if (tileX >= 0 && tileX < width && tileY >= 0 && tileY < height) {
            // Create a new PlacableObject at this tile

            //TODO check if buildings are not present when making a new one

            buildingCount[selectedBuildingIndex] += 1;

            switch (selectedBuildingIndex){
                case 0:
                    placeableObjects.add(new Accommodation(tileX, tileY));
                    break;
                case 1:
                    placeableObjects.add(new SportsCentre(tileX, tileY));
                    break;
                default:
                    System.out.println("NO building for that YET");

            }

        }
    }


    private void drawBuildings(SpriteBatch batch) {
        batch.begin();

        for (PlaceableObject building: placeableObjects){
            building.draw(batch, this.originX, this.originY, this.tileWidth, this.tileHeight);
        }

        batch.end();
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float mouseWorldX, float mouseWorldY) {
        drawGrid(shapeRenderer);
        drawHoweveredCell(shapeRenderer, mouseWorldX, mouseWorldY);

        drawBuildings(batch);
    }


    private int[] screenToTile(float worldX, float worldY) {
        // This is flawed and does not work after resizing for some reason
        // Presumably since the map's origin should be recalculated after resizing
        // But then something else breaks...

        //TODO fix cursor offset near screen edges

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
    public void setTileSizeAndOrigin() {

        tileWidth = 2f * virtualWidth / (width + height);
        tileHeight = tileWidth / 2f; // Ensuring isometric tiles

        // Calculate total grid dimensions
        float totalGridWidth = ((width + height - 2) * (tileWidth / 2f)) + tileWidth;
        float totalGridHeight = ((width + height - 2) * (tileHeight / 2f)) + tileHeight;

        originX = (virtualWidth - totalGridWidth) / 2f + virtualWidth/2f;
        originY = (virtualHeight - totalGridHeight) / 2f;
    }

    public void selectBuilding(int buildingIndex) {

        selectedBuildingIndex = buildingIndex;
        System.out.println("Building " + buildingIndex + " selected");
    }

    public int getSelectedBuildingIndex(){
        return selectedBuildingIndex;
    }

    public int[] getBuildingCount(){
        return buildingCount;
    }

    public void dispose() {
        backgroundTexture.dispose();
    }
}
