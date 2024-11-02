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

    public float getTextureHeight(){
        return 1.0f;
    }

    public float getXOffset(){
        return 0.0f;
    }

    public float getYOffset(){
        return 0.0f;
    }


    public void draw(SpriteBatch batch, float worldX, float worldY, float tileWidth, float tileHeight) {


        // Draw the texture
        batch.draw(texture, worldX - this.width * tileWidth / 2 + tileWidth * getXOffset()
            , worldY - this.height * tileHeight + tileHeight * getYOffset(),
            this.width * tileWidth, this.height * tileHeight * getTextureHeight());
    }

    public void dispose() {
        textureCache.remove(((FileTextureData) texture.getTextureData()).getFileHandle().path());
        texture.dispose();
    }
}
