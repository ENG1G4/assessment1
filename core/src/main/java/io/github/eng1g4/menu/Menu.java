package io.github.eng1g4.menu;

import io.github.eng1g4.util.TextHelper;

public abstract class Menu implements IMenu {

    protected final TextHelper textHelper;

    public Menu(TextHelper textHelper) {
        this.textHelper = textHelper;
    }

}
