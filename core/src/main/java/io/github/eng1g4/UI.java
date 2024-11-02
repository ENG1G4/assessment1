package io.github.eng1g4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import io.github.eng1g4.building.BuildingManager;
import io.github.eng1g4.building.BuildingType;

public class UI {
    private final Stage stage;
    private final Label[] buildingCountText;
    private Label buildingSelectionIndexLabel;
    private final Main main; // This could be bad practice
    private final Viewport viewport;
    private final Camera camera;
    private final BuildingManager buildingManager;

    public UI(Viewport viewport, Camera camera, Main main, BuildingManager buildingManager) {
        this.main = main;
        this.viewport = viewport;
        this.camera = camera;
        this.buildingManager = buildingManager;

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
        float startX = 10;
        float y = 10;

        for (BuildingType buildingType: BuildingType.cachedValues) {
            TextButton buildingButton = new TextButton(buildingType.getName(), skin);
            buildingButton.setSize(buttonWidth, buttonHeight);

            float x = startX + buildingType.ordinal() * (buttonWidth + spacing);
            buildingButton.setPosition(x, y);

            buildingButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    main.getMap().selectBuilding(buildingType);

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

        for (BuildingType buildingType: BuildingType.cachedValues) {
            Label buildingLabel = new Label(buildingType.getName() + " count: " + buildingManager.getBuildingCount(buildingType),
                skin);
            buildingLabel.setSize(labelWidth, labelHeight);

            float y = startY - buildingType.ordinal() * (labelSpacing + labelHeight);
            buildingLabel.setPosition(x, y);

            stage.addActor(buildingLabel);
            buildingCountText[buildingType.ordinal()] = buildingLabel;
        }
    }

    private void createSelectedBuildingLabel(Skin skin) {
        buildingSelectionIndexLabel = new Label("Selected building: " + main.getMap().getSelectedBuilding().getName(),
            skin);
        buildingSelectionIndexLabel.setSize(200, 25);
        buildingSelectionIndexLabel.setPosition(viewport.getWorldWidth() - 220, viewport.getWorldHeight() - 50);
        stage.addActor(buildingSelectionIndexLabel);
    }

    public void drawBuildingLabels() {
        for (BuildingType buildingType: BuildingType.cachedValues) {
            buildingCountText[buildingType.ordinal()]
                .setText(buildingType.getName() + " count: " + buildingManager.getBuildingCount(buildingType));
        }
        buildingSelectionIndexLabel.setText("Selected building: " + main.getMap().getSelectedBuilding().getName());
    }

    public void draw() {
        stage.act();
        drawBuildingLabels();
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
          return switch (keycode) {
            case Keys.P -> {
              main.togglePause();
              yield true;
            }
            case Keys.C -> {
              main.toggleCredits();
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

            main.setMouseX(screenX);
            main.setMouseY(screenY);
            return true;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
