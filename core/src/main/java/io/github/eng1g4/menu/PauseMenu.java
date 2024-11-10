package io.github.eng1g4.menu;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.eng1g4.SoundManager;
import io.github.eng1g4.state.GameStateManager;
import io.github.eng1g4.util.TextHelper;

public class PauseMenu extends Menu {

    // Calculates text geometry once, rather than every frame.
    private final GlyphLayout pausedGlyphLayout = textHelper.getGlyphLayout("Paused");
    private final GlyphLayout creditsGlyphLayout = textHelper.getGlyphLayout("Accommodation, Lecture theatre, Restaurant and Sports centre assets designed by Freepik.");
    private final GlyphLayout pauseInstructionGlyphLayout = textHelper.getGlyphLayout("Press P to play/pause the game.");
    private final GlyphLayout creditsInstructionGlyphLayout = textHelper.getGlyphLayout("Press C to show credits.");
    private final GlyphLayout placingInstructionGlyphLayout = textHelper.getGlyphLayout("Left click to place a building.");

    private boolean showCredits;

    private final Viewport viewport;
    private final Stage pauseStage;


    public PauseMenu(TextHelper textHelper, Viewport viewport, GameStateManager gameStateManager,
        Skin skin, SoundManager soundManager) {
        super(textHelper);
        this.viewport = viewport;
        this.pauseStage = new Stage(viewport);

        createPauseButton(gameStateManager, skin);
        createVolumeSliders(skin, soundManager);
    }

    public Stage getStage() {
        return this.pauseStage;
    }

    public void toggleCredits() {
        this.showCredits = !this.showCredits;
    }

    private void createPauseButton(GameStateManager gameStateManager, Skin skin) {
        TextButton pauseStagePauseButton = new TextButton("Pause", skin);
        pauseStagePauseButton.setPosition(10, viewport.getWorldHeight() - pauseStagePauseButton.getHeight() - 10);
        pauseStagePauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameStateManager.togglePaused();
            }
        });
        pauseStage.addActor(pauseStagePauseButton);
    }

    private void createVolumeSliders(Skin skin, SoundManager soundManager) {
        Label soundEffectLabel = new Label("Sound Effect Volume", skin);
        soundEffectLabel.setPosition(10, viewport.getWorldHeight() - 150);
        pauseStage.addActor(soundEffectLabel);

        Slider soundEffectSlider = new Slider(0, 1.0f, 0.01f, false, skin);
        soundEffectSlider.setValue(soundManager.getSoundEffectVolume());
        soundEffectSlider.setPosition(200, viewport.getWorldHeight() - 150);
        soundEffectSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setSoundEffectVolume(soundEffectSlider.getValue());
            }
        });
        pauseStage.addActor(soundEffectSlider);

        Label musicVolumeLabel = new Label("Music Volume", skin);
        musicVolumeLabel.setPosition(10, viewport.getWorldHeight() - 200);
        pauseStage.addActor(musicVolumeLabel);

        Slider musicVolumeSlider = new Slider(0, 1.0f, 0.01f, false, skin);
        musicVolumeSlider.setValue(soundManager.getMusicVolume());
        musicVolumeSlider.setPosition(200, viewport.getWorldHeight() - 200);
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setMusicVolume(musicVolumeSlider.getValue());
            }
        });
        pauseStage.addActor(musicVolumeSlider);
    }

    @Override
    public void draw() {
        // Each line of text is approximately 20px in height, so offset drawing each line by that.
        textHelper.drawCentredGlyphLayout(this.pausedGlyphLayout);
        textHelper.drawCentredGlyphLayout(this.pauseInstructionGlyphLayout, 0, -40);
        textHelper.drawCentredGlyphLayout(this.creditsInstructionGlyphLayout, 0, -60);
        textHelper.drawCentredGlyphLayout(this.placingInstructionGlyphLayout, 0, -80);

        this.pauseStage.act();
        this.pauseStage.draw();

        if (showCredits) {
            textHelper.drawCentredGlyphLayout(this.creditsGlyphLayout, 0, -120);
        }
    }
}
