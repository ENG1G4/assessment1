package io.github.eng1g4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.eng1g4.building.BuildingManager;
import io.github.eng1g4.building.BuildingType;
import io.github.eng1g4.state.GameStateManager;

public class UI {
    private final Stage stage;
    private final Stage pauseStage;
    private final Label[] buildingCountText;
    private Label buildingSelectionIndexLabel;
    private final Map map;
    private final Viewport viewport;
    private final BuildingManager buildingManager;

    public UI(Viewport viewport, Map map, BuildingManager buildingManager, GameInputProcessor gameInputProcessor, GameStateManager gameStateManager) {
        this.viewport = viewport;
        this.map = map;
        this.buildingManager = buildingManager;

        stage = new Stage(viewport);
        pauseStage = new Stage(viewport);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        createPauseButton(skin, gameStateManager);

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
        inputMultiplexer.addProcessor(pauseStage);

        inputMultiplexer.addProcessor(gameInputProcessor);

        // Set the InputMultiplexer as the input processor
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void createPauseButton(Skin skin, GameStateManager gameStateManager) {
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(10, viewport.getWorldHeight() - pauseButton.getHeight() - 10);

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameStateManager.togglePaused();
            }
        });

        pauseStage.addActor(pauseButton);
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
                    map.selectBuilding(buildingType);

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
        buildingSelectionIndexLabel = new Label("Selected building: " + this.map.getSelectedBuilding().getName(),
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
        buildingSelectionIndexLabel.setText("Selected building: " + this.map.getSelectedBuilding().getName());
    }

    public void draw() {
        stage.act();
        drawBuildingLabels();
        stage.draw();
    }
    public void drawPauseButton() {
        pauseStage.act();
        pauseStage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }

}
