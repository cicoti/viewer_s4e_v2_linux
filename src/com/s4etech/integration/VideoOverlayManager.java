package com.s4etech.integration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.dto.CameraDTO;
import com.s4etech.ui.screens.Viewer;
import com.s4etech.util.GstVideoComponent;
import com.s4etech.util.LineViewer;

public class VideoOverlayManager {

	private static final Logger logger = LoggerFactory.getLogger(VideoOverlayManager.class);
	
    // Map para armazenar os overlays
    private static final Map<String, OverlayInfo> overlays = new ConcurrentHashMap<>();

    // Classe para armazenar informações do overlay
    private static class OverlayInfo {
        JInternalFrame internalFrame;
        JLabel overlayLabel;

        OverlayInfo(JInternalFrame frame, JLabel label, JLayeredPane pane, LineViewer.DrawPanel panel) {
            this.internalFrame = frame;
            this.overlayLabel = label;
        }
    }

    // Adicionar overlay ao InternalFrame
    public static void addOverlay(CameraDTO camera, JInternalFrame internalFrame, GstVideoComponent videoComponent, String overlayText) {
        internalFrame.setLayout(new BorderLayout());
        internalFrame.setBorder(null); // Remove a borda

        // Criando o JLayeredPane para sobrepor elementos
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        // Criando o JLabel que exibe o texto sobreposto
        JLabel overlayLabel = new JLabel(overlayText);
        overlayLabel.setFont(new Font("Roboto", Font.BOLD, 12));
        overlayLabel.setForeground(Color.WHITE); // Texto branco
        overlayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        overlayLabel.setOpaque(false);

        // Criando o painel do overlay que desenha os elementos do JSON
        LineViewer.DrawPanel drawPanel = new LineViewer(camera).new DrawPanel(camera);
        drawPanel.setOpaque(false); // Garante que o vídeo continue visível
        drawPanel.loadFromFile(camera.getCodigo().concat(".json")); // Carrega os desenhos do JSON

        // Adicionando os componentes ao layeredPane
        layeredPane.add(videoComponent, Integer.valueOf(1)); // Vídeo no fundo
        layeredPane.add(drawPanel, Integer.valueOf(2));      // Desenho carregado do JSON
        layeredPane.add(overlayLabel, Integer.valueOf(3));   // Texto sobreposto

        // Ajustando tamanho do overlay automaticamente
        adjustOverlaySize(internalFrame, layeredPane, videoComponent, overlayLabel, drawPanel);

        // Adicionando listener para redimensionamento
        internalFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustOverlaySize(internalFrame, layeredPane, videoComponent, overlayLabel, drawPanel);
            }
        });

        // Adicionando o layeredPane ao InternalFrame
        internalFrame.add(layeredPane, BorderLayout.CENTER);

        // Salvando as informações no Map
        overlays.put(camera.getCodigo(), new OverlayInfo(internalFrame, overlayLabel, layeredPane, drawPanel));
    }

    // Atualizar texto do overlay
    public static void updateOverlayText(CameraDTO camera, String newText) {
        if (camera == null || newText == null) {
            return;
        }

        OverlayInfo overlayInfo = overlays.get(camera.getCodigo());
        if (overlayInfo == null) {
            return;
        }

        // Expressão regular para capturar "FPS: xx"
        String fpsRegex = "FPS:\\s*(\\d+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(fpsRegex);
        java.util.regex.Matcher matcher = pattern.matcher(newText);

        if (matcher.find()) {
            int fps = Integer.parseInt(matcher.group(1));
            String color;

            if (fps >= 9) {
                color = "white"; // Mantém a cor padrão
            } else if (fps >= 3) {
                color = "orange";
                logger.warn("FPS para câmera {} está moderado ({}).", camera.getCodigo(), fps);
            } else {
                color = "red";
                logger.error("FPS para câmera {} está crítico ({}).", camera.getCodigo(), fps);
            }

            // Substituir a parte "FPS: xx" pelo mesmo texto colorido via HTML
            String fpsColored = "<span style='color:" + color + "; font-weight:bold;'>FPS: " + fps + "</span>";
            String updatedText = newText.replaceAll(fpsRegex, fpsColored);

            // Definir o texto formatado no JLabel
            SwingUtilities.invokeLater(() -> {
                overlayInfo.overlayLabel.setText("<html>" + updatedText + "</html>");
            });
        } else {
            // Caso não tenha FPS na string, apenas atualiza normalmente
            SwingUtilities.invokeLater(() -> {
                overlayInfo.overlayLabel.setText(newText);
            });
        }
    }



    // Ajustar tamanho do overlay ao redimensionar
    private static void adjustOverlaySize(JInternalFrame frame, JLayeredPane pane, GstVideoComponent video, JLabel label, LineViewer.DrawPanel panel) {
        int width = frame.getWidth();
        int height = frame.getHeight();

        pane.setBounds(0, 0, width, height);
        video.setBounds(0, 0, width, height);
        panel.setBounds(0, 0, width, height);
        panel.updateOverlaySize(width, height);

        // Posiciona o texto na parte inferior do vídeo
        label.setBounds(10, height - 30, width - 20, 30);
    }

    // Remover overlay
    public static void removeOverlay(String cameraId) {
        if (cameraId == null || cameraId.isBlank()) {
            //logger.warn("removeOverlay chamado com cameraId inválido: {}", cameraId);
            return;
        }

        OverlayInfo overlayInfo = overlays.remove(cameraId);
        
        if (overlayInfo != null) {
            SwingUtilities.invokeLater(() -> {
                overlayInfo.internalFrame.dispose();
                //logger.info("Overlay removido e internalFrame fechado para a câmera {}", cameraId);
            });
        } else {
            //logger.warn("Nenhum overlay encontrado para a câmera {}", cameraId);
        }
    }

}
