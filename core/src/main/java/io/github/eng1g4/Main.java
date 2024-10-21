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


import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.eng1g4.building.BuildingManager;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    private boolean isPaused;
    private Map map;
    private BitmapFont font;
    private OrthographicCamera camera;
    Viewport viewport;
    private UI ui;
    private final CountdownTimer countdownTimer = new CountdownTimer();

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("libgdx.png");
        isPaused = true;

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        float virtualHeight = 720; // You can choose any value
        float aspectRatio = 16f / 9f;
        float virtualWidth = virtualHeight * aspectRatio;

        camera = new OrthographicCamera();
        viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();

        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        BuildingManager buildingManager = new BuildingManager();

        map = new Map("testgrid.jpg",75, 75, virtualWidth, virtualHeight, buildingManager);

        // Create Ui instance
        ui = new UI(viewport, camera, this, buildingManager);

    }

    public void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            countdownTimer.stop();
        } else {
            countdownTimer.start();
        }

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
        String glyphText = "FPS: " + fps + "\nTime Remaining: " + countdownTimer.getTimeRemaining();
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, glyphText);
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
