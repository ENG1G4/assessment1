package io.github.eng1g4.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.Rectangle;
import java.util.HashMap;

public class PlaceableObject extends Rectangle {
    private static final HashMap<String, Texture> textureCache = new HashMap<>();
    private final Texture texture;

    public PlaceableObject(String texturePath, int width, int height, int x, int y) {
        super(x, y, width, height);

        // Use texture cache to avoid loading the same texture multiple times
        if (textureCache.containsKey(texturePath)) {
            this.texture = textureCache.get(texturePath);
        } else {
            this.texture = new Texture(Gdx.files.internal(texturePath));
            textureCache.put(texturePath, this.texture);
        }
    }

    public void draw(SpriteBatch batch, float originX, float originY, float tileWidth, float tileHeight) {
        // Calculate the screen position based on the isometric projection
        float screenX = originX + (this.x - this.y) * (tileWidth / 2f);
        float screenY = originY + (this.x + this.y) * (tileHeight / 2f);

        // Adjust for the object's dimensions
        screenX -= (this.width - 1) * (tileWidth / 2f);
        screenY -= (this.height - 1) * (tileHeight / 2f);

        // Draw the texture
        batch.draw(texture, screenX, screenY, this.width * tileWidth, this.height * tileHeight);
    }

    public void dispose() {
        textureCache.remove(((FileTextureData) texture.getTextureData()).getFileHandle().path());
        texture.dispose();
    }
}
