package com.s4etech.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.s4etech.dto.CameraDTO;
import com.s4etech.ui.screens.Viewer;

public class RotationOverlayPanel extends JPanel {

    private static final long serialVersionUID = 3152494908561796052L;
    
    private String text;
    private JButton rotateButton;
    private JButton flipButton;
    private JButton resizeButton;
    
    // Variável para manipular o viewer e recriar a pipeline
    private Viewer viewer;
    private int cameraIndex; // Identificador da câmera em questão
    
    // Arrays para opções de rotação e espelhamento
    private String[] rotateOptions = {"0", "90l", "90r", "180"};
    private String[] flipOptions = {"none", "horizontal-flip", "vertical-flip"};
    private int rotateIndex = 0; // Índice inicial para rotação
    private int flipIndex = 0;   // Índice inicial para espelhamento
        
    private void configureButton(JButton button, Color color, Font font) {
        button.setBackground(color);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    public RotationOverlayPanel(Viewer viewer, CameraDTO camera) {
    	
        this.viewer = viewer;
        
        this.text = "";
        
        Color pastelGreen = new Color(156, 220, 156); // Um verde pastel suave
        Font font = new Font("Arial", Font.BOLD, 12);
                
        // Configuração dos botões de rotação horizontal e vertical
        rotateButton = new JButton("Rotation");
        configureButton(rotateButton, pastelGreen, font);
        
        flipButton = new JButton("Flip");
        configureButton(flipButton, pastelGreen, font);
        
        resizeButton = new JButton("Re-Size");
        configureButton(resizeButton, pastelGreen, font);
                
        Dimension buttonSize = new Dimension(80, 35); // Largura e altura desejadas para os botões

        // Listeners para os botões
        rotateButton.addMouseListener(new MouseAdapter() {
        	  @Override
              public void mouseEntered(MouseEvent e) {
                  // Manter o OverlayPanel visível
                  RotationOverlayPanel.this.setVisible(true);
              }

              @Override
              public void mouseExited(MouseEvent e) {
                  // Manter o OverlayPanel visível
                  RotationOverlayPanel.this.setVisible(true);
              }
        });

        flipButton.addMouseListener(new MouseAdapter() {
        	  @Override
              public void mouseEntered(MouseEvent e) {
                  // Manter o OverlayPanel visível
                  RotationOverlayPanel.this.setVisible(true);
              }

              @Override
              public void mouseExited(MouseEvent e) {
                  // Manter o OverlayPanel visível
                  RotationOverlayPanel.this.setVisible(true);
              }
        });
        
        resizeButton.addMouseListener(new MouseAdapter() {
      	  @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                RotationOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                RotationOverlayPanel.this.setVisible(true);
            }
        });
              
        // Parte superior com botões de rotação horizontal (Left e Right)
        JPanel rotateHorizontalPanel = new JPanel(new FlowLayout());
        rotateHorizontalPanel.add(rotateButton);
        rotateHorizontalPanel.add(flipButton);
        rotateHorizontalPanel.add(resizeButton);
        rotateHorizontalPanel.setOpaque(false); // Torna o painel transparente
        
        // Configuração do painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(rotateHorizontalPanel);
        mainPanel.setOpaque(false); // Torna o painel transparente

        add(mainPanel, BorderLayout.CENTER);
         
        setOpaque(false); // Torna o painel transparente
        
        

        rotateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	rotateIndex = (rotateIndex + 1) % rotateOptions.length; // Avança para o próximo valor de rotação
                viewer.recreateCameraPipelineWithRotation(camera, rotateOptions[rotateIndex]);
                //System.out.println("Rotation set to: " + rotateOptions[rotateIndex]);
            }
        });


  
        flipButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	 flipIndex = (flipIndex + 1) % flipOptions.length; // Avança para o próximo valor de flip
                 viewer.recreateCameraPipelineWithFlip(camera, flipOptions[flipIndex]);
                 //System.out.println("Flip method set to: " + flipOptions[flipIndex]);
            }
        });
        
        
        resizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                 viewer.recreateCameraPipelineResize(camera);
                 
            }
        });
      
        
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE); // Cor do texto
        g.setFont(new Font("Arial", Font.PLAIN, 12)); // Fonte do texto
        int x = 10; // Posição X para o texto
        int y = 20; // Posição Y para o texto
        g.drawString(text, x, y);
    }

    public void setText(String text) {
        this.text = text;
        repaint(); // Redesenha o painel para atualizar o texto
    }
}