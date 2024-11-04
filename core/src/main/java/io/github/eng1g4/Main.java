package io.github.eng1g4;

import static com.badlogic.gdx.Gdx.graphics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.eng1g4.building.BuildingManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    private boolean gameOver;
    private boolean isPaused;
    private boolean showCredits;
    private Map map;
    private BitmapFont font;
    private OrthographicCamera camera;
    Viewport viewport;
    private UI ui;
    private CountdownTimer countdownTimer;
    private float mouseX;
    private float mouseY;
    // Calculated text layouts
    private GlyphLayout pauseMenuTextLayout;
    private GlyphLayout gameOverTextLayout;
    private GlyphLayout creditsTextLayout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("libgdx.png");
        isPaused = true;
        countdownTimer = new CountdownTimer(this);

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        float virtualHeight = 720; // You can choose any value
        float aspectRatio = 16f / 9f;
        float virtualWidth = virtualHeight * aspectRatio;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();

        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        BuildingManager buildingManager = new BuildingManager();

        map = new Map("testgrid.jpg",10, 10, virtualWidth, virtualHeight, buildingManager);

        // Create Ui instance
        ui = new UI(viewport, camera, this, buildingManager);

        // Calculate pause menu text once, not every frame
        this.pauseMenuTextLayout = getGlyphLayout("Paused");
        this.gameOverTextLayout = getGlyphLayout("Game Over.");
        this.creditsTextLayout = getGlyphLayout("\n\nAccommodation, Lecture theatre, Restaurant and Sports centre assets designed by Freepik.");

    }
    private GlyphLayout getGlyphLayout(String text) {
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(this.font, text);
        return glyphLayout;
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

    public void endGame() {
        this.gameOver = true;
    }

    public Map getMap() {
        return map;
    }

    private void drawCentredGlyphLayout(GlyphLayout glyphLayout) {
        float x = (viewport.getWorldWidth() - glyphLayout.width) / 2;
        float y = (viewport.getWorldHeight() + glyphLayout.height) / 2;

        this.batch.begin();
        font.draw(batch, glyphLayout, x, y);
        this.batch.end();
    }

    private void drawPauseMenu() {
       drawCentredGlyphLayout(this.pauseMenuTextLayout);
       drawTimeRemaining();

       if (showCredits) {
           drawCentredGlyphLayout(this.creditsTextLayout);
       }

    }

    public void toggleCredits() {
        this.showCredits = !this.showCredits;
    }

    public void setMouseX(float mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(float mouseY) {
        this.mouseY = mouseY;
    }

    private void drawGameOverScreen() {
        drawCentredGlyphLayout(this.gameOverTextLayout);
    }

    private void drawTimeRemaining() {
        String text = "\n\nTime Remaining: " + countdownTimer.getTimeRemaining();

        batch.begin();
        font.draw(batch, text, 10, viewport.getWorldHeight() - 10);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ui.resize(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        System.out.println("FPS: "+ graphics.getFramesPerSecond());
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Vector3 mouseScreenCoords = new Vector3(mouseX, mouseY, 0);
        Vector3 mouseWorldCoords = camera.unproject(mouseScreenCoords);

        // Draw game over screen
        if (this.gameOver) {
            drawGameOverScreen();
            return;
        }

        // Draw pause menu
        if (this.isPaused) {
            drawPauseMenu();
            return;
        }

        // Draw map
        map.draw(batch, shapeRenderer, mouseWorldCoords.x, mouseWorldCoords.y);

        drawTimeRemaining();

        // Draw the UI
        ui.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        ui.getStage().dispose();
        this.map.dispose();
        // Dispose other resources as necessary
    }
}
