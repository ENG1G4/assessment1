package io.github.eng1g4;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;


import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    private boolean isPaused;
    private Map map;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private OrthographicCamera camera;
    Viewport viewport;
    private UI ui;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("libgdx.png");
        isPaused = true;

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        glyphLayout = new GlyphLayout();

        float virtualHeight = 720; // You can choose any value
        float aspectRatio = 16f / 9f;
        float virtualWidth = virtualHeight * aspectRatio;

        camera = new OrthographicCamera();
        viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();

        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        map = new Map("testgrid.jpg",75, 75,virtualWidth, virtualHeight);

        // Create Ui instance
        ui = new UI(viewport, camera, this);
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public Map getMap() {
        return map;
    }

    public void drawPauseText(SpriteBatch batch) {
        String text = "Pause";
        float width = text.length();
        float height = font.getCapHeight();
        float x = (viewport.getWorldWidth() - width) / 2;
        float y = (viewport.getWorldHeight() + height) / 2;

        font.draw(batch, text, x, y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ui.resize(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Vector3 mouseScreenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 mouseWorldCoords = camera.unproject(mouseScreenCoords);

        if (!isPaused){
            map.draw(batch, shapeRenderer, mouseWorldCoords.x, mouseWorldCoords.y);
        } else {
            batch.begin();
            drawPauseText(batch);
            batch.end();
        }

        batch.begin();
        int fps = Gdx.graphics.getFramesPerSecond();
        String fpsText = "FPS: " + fps;
        glyphLayout.setText(font, fpsText);
        font.draw(batch, glyphLayout, 10, viewport.getWorldHeight() - 10);
        batch.end();

        // Draw the UI
        ui.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        ui.getStage().dispose();
        // Dispose other resources as necessary
    }
}
