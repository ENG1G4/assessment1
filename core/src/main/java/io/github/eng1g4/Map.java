package io.github.eng1g4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class Map extends ApplicationAdapter implements Disposable {
    private int width;
    private int height;
    private Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private float originX;
    private float originY;
    private float tileWidth;
    private float tileHeight;

    private float virtualWidth;
    private float virtualHeight;
    private PlacableObject objectToPlace;
    private ArrayList<PlacableObject> placableObjects;

    public Map(String backgroundTexturePath, int width, int height, float virtualWidth, float virtualHeight) {
        this.width = width;
        this.height = height;
        backgroundTexture = new Texture(Gdx.files.internal(backgroundTexturePath));

        placableObjects = new ArrayList<>();
        objectToPlace = new PlacableObject("libgdx.png", 3, 2);

        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        // Set tile dimensions based on the texture size
        tileWidth = virtualWidth / width;
        tileHeight = tileWidth / 2f;

        // Calculate the origin to center the grid over the texture
        originX = 0;
        originY = 0;
        updateTileSizeAndOrigin(virtualWidth, virtualHeight);
    }

    private void drawGrid(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1); // White color for lines

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float screenX = (x - y) * (tileWidth / 2f) + originX;
                float screenY = (x + y) * (tileHeight / 2f) + originY;

                // Calculate the four corners of the diamond (tile)
                float x0 = screenX;
                float y0 = screenY + (tileHeight / 2f);

                float x1 = screenX + (tileWidth / 2f);
                float y1 = screenY + tileHeight;

                float x2 = screenX + tileWidth;
                float y2 = screenY + (tileHeight / 2f);

                float x3 = screenX + (tileWidth / 2f);
                float y3 = screenY;

                // Draw lines between the points
                shapeRenderer.line(x0, y0, x1, y1);
                shapeRenderer.line(x1, y1, x2, y2);
                shapeRenderer.line(x2, y2, x3, y3);
                shapeRenderer.line(x3, y3, x0, y0);
            }
        }

        shapeRenderer.end();
    }

    private void drawHoweveredCell(ShapeRenderer shapeRenderer, float mouseWorldX, float mouseWorldY) {
        // Determine which tile the mouse is over
        int[] hoveredTile = screenToTile(mouseWorldX, mouseWorldY);
        int hoverX = hoveredTile[0];
        int hoverY = hoveredTile[1];

        // First, draw the highlighted tile if it is within the map bounds
        if (hoverX >= 0 && hoverX < width && hoverY >= 0 && hoverY < height) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1f); // Semi-transparent red color

            float screenX = (hoverX - hoverY) * (tileWidth / 2f) + originX;
            float screenY = (hoverX + hoverY) * (tileHeight / 2f) + originY;

            // Calculate the four corners of the diamond (tile)
            float x0 = screenX;
            float y0 = screenY + (tileHeight / 2f);

            float x1 = screenX + (tileWidth / 2f);
            float y1 = screenY + tileHeight;

            float x2 = screenX + tileWidth;
            float y2 = screenY + (tileHeight / 2f);

            float x3 = screenX + (tileWidth / 2f);
            float y3 = screenY;

            // Draw two triangles to fill the diamond
            shapeRenderer.triangle(x0, y0, x1, y1, x2, y2);
            shapeRenderer.triangle(x2, y2, x3, y3, x0, y0);

            shapeRenderer.end();
        }
    }

    public void handleClick(float worldX, float worldY) {
        // Convert world coordinates to tile coordinates
        System.out.println("clicked");
        int[] tileCoords = screenToTile(worldX, worldY);
        int tileX = tileCoords[0];
        int tileY = tileCoords[1];

        // Check if the tile is within the map bounds
        if (tileX >= 0 && tileX < width && tileY >= 0 && tileY < height) {
            // Create a new PlacableObject at this tile
            PlacableObject newObject = new PlacableObject(objectToPlace);
            newObject.setPosition(tileX, tileY);
            placableObjects.add(newObject);
        }
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float mouseWorldX, float mouseWorldY) {
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, virtualWidth, virtualHeight);
        batch.end();

        drawHoweveredCell(shapeRenderer, mouseWorldX, mouseWorldY);
        drawGrid(shapeRenderer);

        batch.begin();
        for (PlacableObject obj : placableObjects) {
            obj.draw(batch, originX, originY, tileWidth, tileHeight);
        }
        batch.end();
    }


    private int[] screenToTile(float worldX, float worldY) {
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
    public void updateTileSizeAndOrigin(float virtualWidth, float virtualHeight) {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;

        tileWidth = 2f * virtualWidth / (width + height);
        tileHeight = tileWidth / 2f; // Ensuring isometric tiles

        // Calculate total grid dimensions
        float totalGridWidth = ((width + height - 2) * (tileWidth / 2f)) + tileWidth;
        float totalGridHeight = ((width + height - 2) * (tileHeight / 2f)) + tileHeight;

        // Recalculate origin to center the grid over the background texture
        originX = (virtualWidth - totalGridWidth) / 2f + virtualWidth/2f;
        originY = (virtualHeight - totalGridHeight) / 2f;
    }




    public void dispose() {
        backgroundTexture.dispose();
    }
}
