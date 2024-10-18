package io.github.eng1g4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;

public class PlacableObject {
    private static final HashMap<String, Texture> textureCache = new HashMap<>();
    private final Texture texture;
    private final int width; // in grid tiles
    private final int height; // in grid tiles
    private int tileX; // grid position
    private int tileY;
	private final Color color;

    public PlacableObject(String texturePath, int width, int height, int x, int y, Color color) {
        // Use texture cache to avoid loading the same texture multiple times
        if (textureCache.containsKey(texturePath)) {
            this.texture = textureCache.get(texturePath);
        } else {
            this.texture = new Texture(Gdx.files.internal(texturePath));
            textureCache.put(texturePath, this.texture);
        }
        this.width = width;
        this.height = height;
        this.tileX = x;
        this.tileY = y;
		this.color = color;
    }

    protected int getTileX(){
        return tileX;
    }
    protected int getTileY(){
        return tileY;
    }

    protected int getWidth(){
        return width;
    }

    protected int getHeight(){
        return height;
    }

	public Color getColor(){
		return color;
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
