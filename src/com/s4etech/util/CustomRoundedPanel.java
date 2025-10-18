package com.s4etech.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class CustomRoundedPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CustomRoundedPanel(int arcWidth, int arcHeight) {
        setOpaque(false); // ❗ Importante
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fundo semi-transparente
        g2.setColor(new Color(0, 0, 0, 180)); // fundo preto translúcido
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        g2.dispose();
        super.paintComponent(g);
    }

    private final int arcWidth;
    private final int arcHeight;
}

