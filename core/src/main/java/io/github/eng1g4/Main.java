package io.github.eng1g4;

import static com.badlogic.gdx.Gdx.graphics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.eng1g4.building.BuildingManager;
import io.github.eng1g4.menu.GameOverMenu;
import io.github.eng1g4.menu.PauseMenu;
import io.github.eng1g4.state.GameStateManager;
import io.github.eng1g4.util.TextHelper;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    private Map map;
    private BitmapFont font;
    private OrthographicCamera camera;
    Viewport viewport;
    private UI ui;
    private float mouseX;
    private float mouseY;
    private GameOverMenu gameOverMenu;
    private PauseMenu pauseMenu;
    private GameStateManager gameStateManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("libgdx.png");
        gameStateManager = new GameStateManager();

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        // Window properties
        float virtualHeight = 720; // You can choose any value
        float aspectRatio = 16f / 9f;
        float virtualWidth = virtualHeight * aspectRatio;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();

        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        BuildingManager buildingManager = new BuildingManager();

        // Create map, sets grid size
        map = new Map("testgrid.jpg",100, 100, virtualWidth, virtualHeight, buildingManager);

        // Instantiate menus
        TextHelper textHelper = new TextHelper(viewport, batch, font);
        this.pauseMenu = new PauseMenu(textHelper);
        this.gameOverMenu = new GameOverMenu(textHelper);

        // Create Ui instance
        GameInputProcessor gameInputProcessor = new GameInputProcessor(gameStateManager, pauseMenu, camera, map, this);
        ui = new UI(viewport, map, buildingManager, gameInputProcessor, gameStateManager);

    }

    public void setMouseX(float mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(float mouseY) {
        this.mouseY = mouseY;
    }

    private void drawTimeRemaining() {
        String text = "\n\nTime Remaining: " + this.gameStateManager.getCountdownTimer().getTimeRemaining();

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
        if (this.gameStateManager.isGameOver()) {
            this.gameOverMenu.draw();
            return;
        }

        // Include pause button and time remaining in pause menu and game screen.
        ui.drawPauseButton();
        drawTimeRemaining();

        // Draw pause menu
        if (this.gameStateManager.isGamePaused()) {
            this.pauseMenu.draw();
            return;
        }

        // Draw map
        map.draw(batch, shapeRenderer, mouseWorldCoords.x, mouseWorldCoords.y);

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
