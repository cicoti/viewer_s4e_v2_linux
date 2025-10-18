package com.s4etech.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WatermarkManager {
    private static final Logger logger = LoggerFactory.getLogger(WatermarkManager.class);

    private final JLayeredPane layeredPane;
    private final Image watermarkImage;

    public WatermarkManager(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/icons/logo_marcadagua.png"));
        this.watermarkImage = logoIcon.getImage();
    }

    public void addWatermarkPanels() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        logger.info("Número de monitores detectados: {}", gs.length);

        int minX = Integer.MAX_VALUE;
        List<Rectangle> monitorBoundsList = new ArrayList<>();

        for (int i = 0; i < gs.length; i++) {
            GraphicsConfiguration gc = gs[i].getDefaultConfiguration();
            Rectangle bounds = gc.getBounds();

            monitorBoundsList.add(bounds);
            minX = Math.min(minX, bounds.x);

            logger.info("Monitor {} - ID: {}, Bounds: {}", i, gs[i].getIDstring(), bounds);
        }

        // Adiciona a marca d’água para cada monitor
        for (int i = 0; i < monitorBoundsList.size(); i++) {
            Rectangle bounds = monitorBoundsList.get(i);

            JPanel watermarkOverlay = new JPanel() {
                private static final long serialVersionUID = 1L;

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    int x = (getWidth() - watermarkImage.getWidth(null)) / 2;
                    int y = (getHeight() - watermarkImage.getHeight(null)) / 2;
                    g.drawImage(watermarkImage, x, y, this);
                }
            };

            watermarkOverlay.setOpaque(false);
            watermarkOverlay.setBounds(bounds.x - minX, bounds.y, bounds.width, bounds.height);

            logger.info("Renderizando marca d’água no monitor {} nas coordenadas ajustadas: {}", i, watermarkOverlay.getBounds());

            layeredPane.add(watermarkOverlay, JLayeredPane.DEFAULT_LAYER);
        }

        // Verificação dos componentes no `layeredPane`
        for (Component comp : layeredPane.getComponents()) {
            logger.info("Componente no layeredPane: {} Bounds: {}", comp.getClass().getName(), comp.getBounds());
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }
}