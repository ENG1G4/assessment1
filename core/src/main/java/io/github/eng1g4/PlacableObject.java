package io.github.eng1g4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class PlacableObject {
    private static HashMap<String, Texture> textureCache = new HashMap<>();
    private Texture texture;
    private int width; // in grid tiles
    private int height; // in grid tiles
    private int tileX; // grid position
    private int tileY;

    public PlacableObject(String texturePath, int width, int height) {
        // Use texture cache to avoid loading the same texture multiple times
        if (textureCache.containsKey(texturePath)) {
            this.texture = textureCache.get(texturePath);
        } else {
            this.texture = new Texture(Gdx.files.internal(texturePath));
            textureCache.put(texturePath, this.texture);
        }
        this.width = width;
        this.height = height;
    }

    // Copy constructor
    public PlacableObject(PlacableObject other) {
        this.texture = other.texture;
        this.width = other.width;
        this.height = other.height;
    }

    public void setPosition(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public void draw(SpriteBatch batch, float originX, float originY, float tileWidth, float tileHeight) {
        // Calculate the screen position based on the isometric projection
        float screenX = originX + (tileX - tileY) * (tileWidth / 2f);
        float screenY = originY + (tileX + tileY) * (tileHeight / 2f);

        // Adjust for the object's dimensions
        screenX -= (width - 1) * (tileWidth / 2f);
        screenY -= (height - 1) * (tileHeight / 2f);

        // Draw the texture
        batch.draw(texture, screenX, screenY, width * tileWidth, height * tileHeight);
    }

    public void dispose() {

    }
}
