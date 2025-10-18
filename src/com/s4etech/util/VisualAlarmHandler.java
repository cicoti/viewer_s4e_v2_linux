package com.s4etech.util;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisualAlarmHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(VisualAlarmHandler.class);
	
    private final JFrame frame;
    private final JLabel alertLabel;
    private boolean isActive = false;
    private Thread blinkThread;

    public VisualAlarmHandler() {
        // Criando a janela do alarme
        frame = new JFrame();
        frame.setSize(200, 200);
        frame.setUndecorated(true); // Remove bordas e título
        frame.setAlwaysOnTop(true);
        frame.setBackground(new Color(0, 0, 0, 0)); // Fundo transparente

        // Criando um painel com fundo transparente
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setOpaque(false); // Painel sem fundo visível

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        // Ícone de alerta
        alertLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/alarme.png")));
        alertLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundPanel.add(alertLabel, gbc);

        // Adiciona o painel ao frame
        frame.getContentPane().setBackground(new Color(0, 0, 0, 0)); // Fundo do frame transparente
        frame.getContentPane().add(backgroundPanel);
        frame.setVisible(false);
    }

    public void activateAlarm() {
        if (!isActive) {
            isActive = true;
            SwingUtilities.invokeLater(() -> frame.setVisible(true));
            startBlinkEffect();
        }
    }

    public void deactivateAlarm() {
        isActive = false;
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
            alertLabel.setVisible(true); // Garante que o ícone fique visível quando desligado
        });
    }

    private void startBlinkEffect() {
        blinkThread = new Thread(() -> {
            while (isActive) {
                try {
                    SwingUtilities.invokeLater(() -> alertLabel.setVisible(!alertLabel.isVisible())); // Alterna visibilidade
                    Thread.sleep(500); // Pisca a cada 500ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            SwingUtilities.invokeLater(() -> alertLabel.setVisible(true)); // Garante que o ícone fique visível ao desligar
        });
        blinkThread.setDaemon(true);
        blinkThread.start();
    }
}
