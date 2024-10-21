package io.github.eng1g4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Vector3;

public class UI {
    private final Stage stage;
    private final Label[] buildingCountText;
    private Label buildingSelectionIndexLabel;
    private final Main main; // This could be bad practice
    private final Viewport viewport;
    private final Camera camera;

    public UI(Viewport viewport, Camera camera, Main main) {
        this.main = main;
        this.viewport = viewport;
        this.camera = camera;

        stage = new Stage(viewport);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create pause button
        createPauseButton(skin);

        // Create building selection buttons
        buildingCountText = new Label[5];
        createBuildingButtons(skin);

        // Create the "building x count: y" labels
        createBuildingCountLabels(skin);

        // Create the "Selected building: " label
        createSelectedBuildingLabel(skin);


        // Handle game inputs. Ui stage is required to be inputprocessor, so multiplexer
        // is needed if other inputs are added.
         InputMultiplexer inputMultiplexer = new InputMultiplexer();

        // Add the Stage's input processor first, so it has priority
        inputMultiplexer.addProcessor(stage);

        inputMultiplexer.addProcessor(new GameInputProcessor());

        // Set the InputMultiplexer as the input processor
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void createPauseButton(Skin skin) {
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(10, viewport.getWorldHeight() - pauseButton.getHeight() - 10);

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.togglePause();
            }
        });

        stage.addActor(pauseButton);
    }

    private void createBuildingButtons(Skin skin) {
        float buttonWidth = 100;
        float buttonHeight = 50;
        float spacing = 10;
        float totalWidth = 5 * buttonWidth + 4 * spacing;
        float startX = (viewport.getWorldWidth() - totalWidth) / 2f;
        float y = 10;

        for (int i = 0; i < 5; i++) {
            TextButton buildingButton = new TextButton("Building " + (i + 1), skin);
            buildingButton.setSize(buttonWidth, buttonHeight);

            float x = startX + i * (buttonWidth + spacing);
            buildingButton.setPosition(x, y);

            final int index = i;
            buildingButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    main.getMap().selectBuilding(index);

                }
            });

            stage.addActor(buildingButton);
        }
    }

    private void createBuildingCountLabels(Skin skin) {
        float labelWidth = 150;
        float labelHeight = 25;
        float labelSpacing = 5;
        float startY = viewport.getWorldHeight() - 100;
        float x = 10;

        int[] buildingCount = main.getMap().getBuildingCount();

        for (int i = 0; i < 5; i++) {
            Label buildingLabel = new Label("Building "+ (i + 1) + " count: " + buildingCount[i],
                skin);
            buildingLabel.setSize(labelWidth, labelHeight);

            float y = startY - i * (labelSpacing + labelHeight);
            buildingLabel.setPosition(x, y);

            stage.addActor(buildingLabel);
            buildingCountText[i] = buildingLabel;
        }
    }

    private void createSelectedBuildingLabel(Skin skin) {
        buildingSelectionIndexLabel = new Label("Selected building: " + (main.getMap().getSelectedBuildingIndex() + 1),
            skin);
        buildingSelectionIndexLabel.setSize(200, 25);
        buildingSelectionIndexLabel.setPosition(viewport.getWorldWidth() - 220, viewport.getWorldHeight() - 50);
        stage.addActor(buildingSelectionIndexLabel);
    }

    public void drawBuildingCount() {
        int[] buildingCount = main.getMap().getBuildingCount();
        for (int i = 0; i < 5; i++) {
            buildingCountText[i].setText("Building " + (i + 1) + " count: " + buildingCount[i]);
        }
        buildingSelectionIndexLabel.setText("Selected building: " + (main.getMap().getSelectedBuildingIndex() + 1));

    }

    public void draw() {
        stage.act();
        drawBuildingCount();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }

    private class GameInputProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == com.badlogic.gdx.Input.Keys.P) {
                main.togglePause();
                return true;
            }
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            if (main.isPaused()) return false;

            if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
                Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
                main.getMap().handleClickLeft(worldCoordinates.x, worldCoordinates.y);
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
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}