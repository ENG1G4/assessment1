package io.github.eng1g4;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

class Building implements Disposable {
    private float x;
    private float y;
    private float width;
    private float height;
    private Texture texture;
    private String type;

    public Building(String type, float x, float y, String texturePath) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.texture = new Texture(texturePath);

        // Set width and height based on texture dimensions
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    // Draws the building onto the screen with a tilted 2D perspective
    public void draw(SpriteBatch batch) {
        // Apply transformation matrix for tilting the building
        Matrix4 transform = new Matrix4();
        transform.translate(x + width / 2, y + height / 2, 0);
        transform.rotate(Vector3.X, 30); // Tilt the building forward by 30 degrees
        transform.translate(-width / 2, -height / 2, 0);

        batch.setTransformMatrix(transform);
        batch.draw(texture, x, y);

        // Reset the transformation matrix after drawing
        batch.setTransformMatrix(new Matrix4());
    }

    // Updates the building's state (if needed)
    public void update(float deltaTime) {
        // Implement any per-frame updates here
    }

    // Disposes of assets when no longer needed
    @Override
    public void dispose() {
        texture.dispose();
    }

    // Getters for building properties
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public String getType() { return type; }
}
