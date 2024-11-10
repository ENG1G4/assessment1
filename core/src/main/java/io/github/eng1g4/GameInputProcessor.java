package io.github.eng1g4;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import io.github.eng1g4.menu.PauseMenu;
import io.github.eng1g4.state.GameStateManager;

public class GameInputProcessor implements InputProcessor {

    private final GameStateManager gameStateManager;
    private final PauseMenu pauseMenu;
    private final Camera camera;
    private final Map map;
    private final Main main;


    public GameInputProcessor(GameStateManager gameStateManager, PauseMenu pauseMenu, Camera camera, Map map, Main main) {
        this.gameStateManager = gameStateManager;
        this.pauseMenu = pauseMenu;
        this.camera = camera;
        this.map = map;
        this.main = main;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return switch (keycode) {
            case Keys.P -> {
                this.gameStateManager.togglePaused();
                yield true;
            }
            case Keys.C -> {
                this.pauseMenu.toggleCredits();
                yield true;
            }
            default -> false;
        };
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (this.gameStateManager.isGamePaused()) return false;

        if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
            Vector3 worldCoordinates = this.camera.unproject(new Vector3(screenX, screenY, 0));
            this.map.handleClickLeft(worldCoordinates.x, worldCoordinates.y);
            System.out.println("Left Clicked @ " + screenX + " " + screenY);
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

        this.main.setMouseX(screenX);
        this.main.setMouseY(screenY);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
