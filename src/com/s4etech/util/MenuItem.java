package com.s4etech.util;

import java.awt.Font;

/**
 * Classe auxiliar para armazenar informações do item de menu.
 */
public class MenuItem {
	
    private final String name;
    private final Font font;
    private final Runnable action;

    public MenuItem(String name, Font font, Runnable action) {
        this.name = name;
        this.font = font;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public Font getFont() {
        return font;
    }

    public Runnable getAction() {
        return action;
    }
}