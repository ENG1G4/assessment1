package io.github.eng1g4.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TextHelper {

    private final Viewport viewport;
    private final SpriteBatch spriteBatch;
    private final BitmapFont font;

    public TextHelper(Viewport viewport, SpriteBatch spriteBatch, BitmapFont font) {
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
        this.font = font;
    }

    public void drawCentredGlyphLayout(GlyphLayout glyphLayout) {
        drawCentredGlyphLayout(glyphLayout, 0, 0);
    }

    public void drawCentredGlyphLayout(GlyphLayout glyphLayout, float x, float y) {
        float xPosition = ((this.viewport.getWorldWidth() - glyphLayout.width) / 2) + x;
        float yPosition = ((this.viewport.getWorldHeight() + glyphLayout.height) / 2) + y;

        this.spriteBatch.begin();
        this.font.draw(this.spriteBatch, glyphLayout, xPosition, yPosition);
        this.spriteBatch.end();
    }

    public GlyphLayout getGlyphLayout(String text) {
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(this.font, text);
        return glyphLayout;
    }

}
