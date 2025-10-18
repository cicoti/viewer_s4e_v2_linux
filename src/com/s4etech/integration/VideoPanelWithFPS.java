package com.s4etech.integration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.ui.screens.Viewer;
import com.s4etech.util.GstVideoComponent;

public class VideoPanelWithFPS extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(VideoPanelWithFPS.class);

    private static final long serialVersionUID = 1L;

    private JLayeredPane layeredPane;
    private JLabel fpsLabel;
    private JLabel camLabel; // Novo JLabel para a câmera
    private JLabel iconLabel;
    private ImageIcon redIcon;
    private ImageIcon yellowIcon;
    private ImageIcon greenIcon;

    public VideoPanelWithFPS(GstVideoComponent videoComponent, String cameraName) {

        setLayout(new BorderLayout());

        // Ícones para indicar o status do FPS
        redIcon = new ImageIcon(Viewer.class.getResource("/icons/fps_red.png"));
        yellowIcon = new ImageIcon(Viewer.class.getResource("/icons/fps_yellow.png"));
        greenIcon = new ImageIcon(Viewer.class.getResource("/icons/fps_green.png"));

        // Inicializa os rótulos
        iconLabel = new JLabel(redIcon);

        fpsLabel = new JLabel("FPS 0");
        fpsLabel.setFont(new Font("Roboto", Font.BOLD, 12));
        fpsLabel.setForeground(Color.WHITE);
        fpsLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        fpsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        camLabel = new JLabel(cameraName); // Inicialização do camLabel
        camLabel.setFont(new Font("Roboto", Font.BOLD, 12));
        camLabel.setForeground(Color.LIGHT_GRAY);
        camLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Configurações do layeredPane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(redIcon.getIconWidth(), redIcon.getIconHeight()));

        iconLabel.setBounds(0, 0, redIcon.getIconWidth(), redIcon.getIconHeight());
        fpsLabel.setBounds(-15, 0, redIcon.getIconWidth(), redIcon.getIconHeight());

        layeredPane.add(iconLabel, Integer.valueOf(1));
        layeredPane.add(fpsLabel, Integer.valueOf(2));

        // Painel adicional para a câmera (fica à esquerda do painel principal)
     // Ajuste no posicionamento do camLabel com 10px de margem
        JPanel camPanel = new JPanel(new BorderLayout());
        camPanel.setOpaque(false);
        camPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Adiciona margem de 10px à esquerda
        camPanel.add(camLabel, BorderLayout.WEST);

        // Painel para FPS (fica à direita, sem alterações no comportamento)
        JPanel fpsPanel = new JPanel(new BorderLayout());
        fpsPanel.setOpaque(false);
        fpsPanel.add(layeredPane, BorderLayout.EAST);

        // Adiciona os dois painéis ao painel principal
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(camPanel, BorderLayout.WEST); // Painel da câmera à esquerda
        bottomPanel.add(fpsPanel, BorderLayout.EAST); // Painel do FPS à direita

        add(videoComponent, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateFPS(int fps) {
        
        fpsLabel.setText("FPS " + fps);

        if (fps >= 9) {
            iconLabel.setIcon(greenIcon);
        } else if (fps >= 3) {
            iconLabel.setIcon(yellowIcon);
        } else {
            iconLabel.setIcon(redIcon);
        }
    }
}
