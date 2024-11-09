package io.github.eng1g4.menu;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import io.github.eng1g4.util.TextHelper;

public class PauseMenu extends Menu {

    // Calculates text geometry once, rather than every frame.
    private final GlyphLayout pausedGlyphLayout = textHelper.getGlyphLayout("Paused");
    private final GlyphLayout creditsGlyphLayout = textHelper.getGlyphLayout("Accommodation, Lecture theatre, Restaurant and Sports centre assets designed by Freepik.");
    private final GlyphLayout pauseInstructionGlyphLayout = textHelper.getGlyphLayout("Press P to play/pause the game.");
    private final GlyphLayout creditsInstructionGlyphLayout = textHelper.getGlyphLayout("Press C to show credits.");
    private final GlyphLayout placingInstructionGlyphLayout = textHelper.getGlyphLayout("Left click to place a building.");

    private boolean showCredits;

    public PauseMenu(TextHelper textHelper) {
        super(textHelper);
    }

    public void toggleCredits() {
        this.showCredits = !this.showCredits;
    }

    @Override
    public void draw() {
        // Each line of text is approximately 20px in height, so offset drawing each line by that.
        textHelper.drawCentredGlyphLayout(this.pausedGlyphLayout);
        textHelper.drawCentredGlyphLayout(this.pauseInstructionGlyphLayout, 0, -40);
        textHelper.drawCentredGlyphLayout(this.creditsInstructionGlyphLayout, 0, -60);
        textHelper.drawCentredGlyphLayout(this.placingInstructionGlyphLayout, 0, -80);

        if (showCredits) {
            textHelper.drawCentredGlyphLayout(this.creditsGlyphLayout, 0, -120);
        }
    }
}
