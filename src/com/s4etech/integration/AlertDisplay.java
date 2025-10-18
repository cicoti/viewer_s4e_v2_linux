package com.s4etech.integration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.s4etech.ui.screens.Viewer;

public class AlertDisplay {

    private static final Map<String, Integer> timerMap = new ConcurrentHashMap<>();
    private static final Map<String, Timer> timerInstances = new ConcurrentHashMap<>();
    private static final Map<String, JLabel> timerLabels = new ConcurrentHashMap<>(); // Map para associar cada label ao timer
    private static final ImageIcon ICON_ALERT = new ImageIcon(Viewer.class.getResource("/icons/alert.png"));

    public static void displayAlert(JInternalFrame internalFrame, String cameraName, String ip) {

        SwingUtilities.invokeLater(() -> {
        	
        	 if (internalFrame == null) {
                 return;
             }
        	
            internalFrame.getContentPane().removeAll();

            JPanel backgroundPanel = new JPanel(new GridBagLayout());
            backgroundPanel.setBackground(Color.BLACK);
            backgroundPanel.setPreferredSize(new Dimension(internalFrame.getWidth(), internalFrame.getHeight()));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.CENTER;

            // Icone de alerta
            JLabel alertLabel = new JLabel(ICON_ALERT);
            alertLabel.setHorizontalAlignment(JLabel.CENTER);
            backgroundPanel.add(alertLabel, gbc);

            // Texto da câmera
            JLabel textLabel = new JLabel(cameraName + " - NO SIGNAL");
            textLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
            textLabel.setForeground(Color.DARK_GRAY);
            gbc.insets = new Insets(10, 0, 0, 0); // Margem acima do texto
            backgroundPanel.add(textLabel, gbc);

            // Texto do IP
            JLabel ipLabel = new JLabel("IP: " + ip);
            ipLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
            ipLabel.setForeground(Color.DARK_GRAY);
            gbc.insets = new Insets(5, 0, 0, 0); // Margem acima do IP
            backgroundPanel.add(ipLabel, gbc);

            // Label do Timer
            JLabel timerLabel = timerLabels.getOrDefault(cameraName, new JLabel("Time: 0s")); // Reutilizar label existente
            timerLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
            timerLabel.setForeground(Color.DARK_GRAY);
            gbc.insets = new Insets(10, 0, 0, 0); // Margem acima do timer
            backgroundPanel.add(timerLabel, gbc);

            // Associar o label ao cameraName se for novo
            timerLabels.putIfAbsent(cameraName, timerLabel);

            // Inicializar o timer (se não estiver ativo)
            startTimer(cameraName);

            // Atualizar o conteúdo do frame
            internalFrame.getContentPane().add(backgroundPanel);
            internalFrame.getContentPane().revalidate();
            internalFrame.getContentPane().repaint();
        });
    }

    public static void startTimer(String cameraName) {
        // Verificar se o timer já foi iniciado
        if (timerInstances.containsKey(cameraName)) {
            //System.out.println("Timer já está ativo para a câmera: " + cameraName);
            return;
        }

        // Inicializar valores
        timerMap.putIfAbsent(cameraName, 0); // Garantir que o valor inicial é 0
        JLabel timerLabel = timerLabels.get(cameraName); // Obter o label associado
        Timer timer = new Timer();

        // Agendar tarefas de atualização
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    int currentTime = timerMap.get(cameraName) + 1;
                    timerMap.put(cameraName, currentTime);
                    if (timerLabel != null) {
                        timerLabel.setText("Time: " + currentTime + "s");
                    }
                });
            }
        }, 0, 1000);

        timerInstances.put(cameraName, timer);
    }

    public static void stopTimer(String cameraName) {
        if (timerInstances.containsKey(cameraName)) {
            timerInstances.get(cameraName).cancel();
            timerInstances.remove(cameraName);
            timerMap.remove(cameraName); // Opcional: reseta o tempo também
        }
    }

    public static void resetTimer(String cameraName) {
        stopTimer(cameraName); // Parar o timer atual
        timerMap.put(cameraName, 0); // Reiniciar o tempo para 0
        JLabel timerLabel = timerLabels.get(cameraName);
        if (timerLabel != null) {
            timerLabel.setText("Time: 0s"); // Atualizar a exibição
        }
    }
}
