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

public class Ui {
    private Stage stage;
    private Skin skin;
    private TextButton pauseButton;
    private TextButton[] buildingButtons;
    private Label[] buildingCountText;
    private Label selectedBuildingLabel;
    private Label buildingSelectionIndexLabel;
    private Main main; // This could be bad practice
    private InputMultiplexer inputMultiplexer;
    private Viewport viewport;
    private Camera camera;

    public Ui(Viewport viewport, Camera camera, Main main) {
        this.main = main;
        this.viewport = viewport;
        this.camera = camera;

        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));


        pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(10, viewport.getWorldHeight() - pauseButton.getHeight() - 10);


        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.togglePause();
            }
        });

        stage.addActor(pauseButton);

        buildingButtons = new TextButton[5];

        float buttonWidth = 100;
        float buttonHeight = 50;
        float spacing = 10;
        float totalWidth = 5 * buttonWidth + 4 * spacing;
        float startX = (viewport.getWorldWidth() - totalWidth) / 2f;
        float y = 10;

        for (int i = 0; i < 5; i++) {
            //final int index is needed for the listener later
            final int index = i;
            buildingButtons[i] = new TextButton("Building " + (i + 1), skin);
            buildingButtons[i].setSize(buttonWidth, buttonHeight);
            float x = startX + i * (buttonWidth + spacing);
            buildingButtons[i].setPosition(x, y);

            buildingButtons[i].addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    main.getMap().selectBuilding(index);

                }
            });

            stage.addActor(buildingButtons[i]);
        }

        buildingCountText = new Label[5];

        float labelWidth = 150;
        float labelHeight = 25;
        float labelSpacing = 5;
        float startY = viewport.getWorldHeight() - 100;
        float x = 10;

        int[] buildingCount = main.getMap().getBuildingCount();

        for (int i = 0; i < 5; i++) {
            buildingCountText[i] = new Label("Building "+ (i + 1) + " count: " + buildingCount[i], skin);
            buildingCountText[i].setSize(labelWidth, labelHeight);
            y = startY - i * (labelSpacing + labelHeight);
            buildingCountText[i].setPosition(x, y);
            stage.addActor(buildingCountText[i]);
        }

        buildingSelectionIndexLabel = new Label("Selected building: " + (main.getMap().getSelectedBuildingIndex() + 1), skin );
        buildingSelectionIndexLabel.setSize(200, 25);
        buildingSelectionIndexLabel.setPosition(viewport.getWorldWidth() - 220, viewport.getWorldHeight() - 50);
        stage.addActor(buildingSelectionIndexLabel);




        // Ui stage is required to be inputprocessor, so multiplexer is needed if
        // other inputs are added
        inputMultiplexer = new InputMultiplexer();

        // Add the Stage's input processor first so it has priority
        inputMultiplexer.addProcessor(stage);

        inputMultiplexer.addProcessor(new GameInputProcessor());

        // Set the InputMultiplexer as the input processor
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void updateBuildingCount(){
        int[] buildingCount = main.getMap().getBuildingCount();
        for (int i = 0; i < 5; i++) {
            buildingCountText[i].setText("Building " + (i + 1) + " count: " + buildingCount[i]);
        }
        buildingSelectionIndexLabel.setText("Selected building: " + (main.getMap().getSelectedBuildingIndex() + 1));

    }

    public void draw() {
        stage.act();
        updateBuildingCount();
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
            if (button == com.badlogic.gdx.Input.Buttons.LEFT || button == com.badlogic.gdx.Input.Buttons.RIGHT) {
                Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
                if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
                    main.getMap().handleClickLeft(worldCoordinates.x, worldCoordinates.y);
                    System.out.println("Left Clicked @ " + screenX + " " + screenY);
                }
//                System.out.println("Score: " + main.getMap().calculateSatisfactionScore());
//                Artifact of the past...
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
