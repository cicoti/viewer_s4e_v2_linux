package com.s4etech.util;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.*;
import com.s4etech.dto.CameraDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LineViewer extends JFrame {
    private static final long serialVersionUID = 6821848554148207651L;
    private static final Logger logger = LoggerFactory.getLogger(LineViewer.class);
    
    private DrawPanel drawPanel;
    private static final Path PATH_MARCACOES = Paths.get(System.getProperty("user.dir"), "configuracao/marcacoes/");
    
    public LineViewer(CameraDTO camera) {
        logger.info("Inicializando LineViewer");
        
        setTitle("Reprodutor de Desenho");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criando o painel de desenho
        drawPanel = new DrawPanel(camera);
        add(drawPanel, BorderLayout.CENTER);

        // Adiciona um listener para atualizar o tamanho do painel quando a janela for redimensionada
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                logger.debug("Janela redimensionada: {}x{}", getContentPane().getWidth(), getContentPane().getHeight());
                drawPanel.updateOverlaySize(getContentPane().getWidth(), getContentPane().getHeight());
            }
        });

        // Botão para recarregar o desenho
        JButton loadButton = new JButton("Carregar Desenho");
        loadButton.addActionListener(e -> {
            logger.info("Botão 'Carregar Desenho' pressionado.");
            drawPanel.loadFromFile(camera.getCodigo());
        });
        add(loadButton, BorderLayout.SOUTH);
        
        logger.info("LineViewer inicializado com sucesso.");
    }

    public class DrawPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private static final Logger logger = LoggerFactory.getLogger(DrawPanel.class);
        
        private List<Line> lines = new ArrayList<>();
        private int panelWidth;
        private int panelHeight;

        public DrawPanel(CameraDTO camera) {
            logger.info("Inicializando DrawPanel.");
            setOpaque(false);
            setPreferredSize(new Dimension(1280, 720));
            
            //logger.info("Carregando desenho ao iniciar.");
            //loadFromFile(camera.getCodigo());
        }

        public void updateOverlaySize(int width, int height) {
            logger.debug("Atualizando overlay para tamanho: {}x{}", width, height);
            this.panelWidth = width;
            this.panelHeight = height;
            repaint();
        }

        public void loadFromFile(String filename) {
            logger.info("Tentando carregar o arquivo de desenho: {}", filename);
            
            File file = PATH_MARCACOES.resolve(filename).toFile();
            if (!file.exists()) {
                logger.warn("Arquivo {} não existe. Nenhum desenho será carregado.", file.getAbsolutePath());
                return;
            }

            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Line[] loadedLines = gson.fromJson(reader, Line[].class);
                lines = new ArrayList<>(Arrays.asList(loadedLines));

                logger.info("Arquivo {} carregado com sucesso. Total de linhas: {}", file.getAbsolutePath(), lines.size());
                repaint();
            } catch (IOException e) {
                logger.error("Erro ao carregar o arquivo {}.", filename, e);
                JOptionPane.showMessageDialog(this, "Erro ao carregar o arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (lines != null && !lines.isEmpty()) {
                double scaleX = (double) getWidth() / 1280.0;
                double scaleY = (double) getHeight() / 720.0;
               
                for (Line line : lines) {
                    g2.setColor(line.getColor());
                    g2.setStroke(line.getStroke());

                    int scaledX1 = (int) (line.x1 * scaleX);
                    int scaledY1 = (int) (line.y1 * scaleY);
                    int scaledX2 = (int) (line.x2 * scaleX);
                    int scaledY2 = (int) (line.y2 * scaleY);
                    
                    g2.drawLine(scaledX1, scaledY1, scaledX2, scaledY2);
                }
            } 
        }
    }

    class Line {
        private static final Logger logger = LoggerFactory.getLogger(Line.class);
        
        int x1, y1, x2, y2;
        String color;
        float thickness;
        boolean dashed;

        public Line(int x1, int y1, int x2, int y2, String color, float thickness, boolean dashed) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.thickness = thickness;
            this.dashed = dashed;
            
            logger.debug("Criada nova linha: {}", this.toString());
        }

        public Color getColor() {
            return switch (color) {
                case "Vermelho" -> Color.RED;
                case "Verde" -> Color.GREEN;
                case "Amarelo" -> Color.YELLOW;
                case "Azul" -> Color.CYAN;
                default -> Color.BLACK;
            };
        }

        public Stroke getStroke() {
            return dashed
                    ? new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, new float[]{10, 10}, 0)
                    : new BasicStroke(thickness);
        }

        @Override
        public String toString() {
            return "Line{x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 +
                    ", color='" + color + "', thickness=" + thickness + ", dashed=" + dashed + "}";
        }
    }
}
