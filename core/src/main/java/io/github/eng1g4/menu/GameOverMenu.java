package io.github.eng1g4.menu;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import io.github.eng1g4.util.TextHelper;

public class GameOverMenu extends Menu {

    // Calculates text geometry once, rather than every frame.
    private final GlyphLayout gameOverGlyphLayout = textHelper.getGlyphLayout("Game Over");

    public GameOverMenu(TextHelper textHelper) {
        super(textHelper);
    }

    @Override
    public void draw() {
        textHelper.drawCentredGlyphLayout(this.gameOverGlyphLayout);
    }
}
