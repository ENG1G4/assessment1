package io.github.eng1g4;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.eng1g4.Map;
import io.github.eng1g4.Building;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements InputProcessor {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    private boolean isPaused;
    private Map map;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private OrthographicCamera camera;
    Viewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("libgdx.png");
        isPaused = true;


        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        glyphLayout = new GlyphLayout();

        float virtualHeight = 720; // You can choose any value
        float aspectRatio = 16f / 9f;
        float virtualWidth = virtualHeight * aspectRatio;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();

        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        map = new Map("testgrid.jpg",75, 75,virtualWidth, virtualHeight);
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            isPaused = !isPaused;
        }


        if (!isPaused) {
            //
        }
    }
    public void drawPauseText(SpriteBatch batch) {


        String text = "Pause";
        float width = text.length();
        float height = font.getCapHeight();
        float x = (Gdx.graphics.getWidth() - width) / 2;
        float y = (Gdx.graphics.getHeight() + height) / 2;

        font.draw(batch, text, x, y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        map.updateTileSizeAndOrigin(width, height);
    }

    @Override
    public void render() {
        handleInput();
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
    }


    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keypresssed " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println(screenX + " " + screenY);
        if (button == Input.Buttons.LEFT) {
            Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
            map.handleClick(worldCoordinates.x, worldCoordinates.y);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
