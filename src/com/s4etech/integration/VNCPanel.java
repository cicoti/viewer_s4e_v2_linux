package com.s4etech.integration;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.config.manager.VNCConfigurationManager;
import com.s4etech.dto.VNCDTO;
import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;
import com.shinyhut.vernacular.client.rendering.ColorDepth;

public class VNCPanel extends JPanel {
    private static final long serialVersionUID = 6697228841017305915L;
    private static final Logger logger = LoggerFactory.getLogger(VNCPanel.class);

    private VernacularClient vncClient;
    private Image lastFrame;
    private VNCDTO vncDTO;
    private final AtomicBoolean isConnecting = new AtomicBoolean(false);

    // controle: 1:1 (false) ou caber no painel (true)
    private volatile boolean scaleToFit = false;

    public VNCPanel() {
        logger.info("Inicializando VNCPanel.");
        initializeVNCClient();
        addMouseListeners();
        manageConnection();
    }

    public void setScaleToFit(boolean scaleToFit) {
        this.scaleToFit = scaleToFit;
        revalidate();
        repaint();
    }

    private void initializeVNCClient() {
        logger.info("Inicializando cliente VNC.");
        vncDTO = VNCConfigurationManager.get();

        VernacularConfig config = new VernacularConfig();
        config.setTargetFramesPerSecond(15);
        config.setErrorListener(e -> logger.error("Erro no cliente VNC", e));
        config.setPasswordSupplier(() -> vncDTO.getSenha());
        config.setUseLocalMousePointer(vncDTO.isCursorLocal());
        config.setEnableCopyrectEncoding(vncDTO.isCopyrect());
        config.setEnableRreEncoding(vncDTO.isRre());
        config.setEnableHextileEncoding(vncDTO.isHextile());
        config.setEnableZLibEncoding(vncDTO.isZlib());

        // cor padrão segura
        ColorDepth depth = ColorDepth.BPP_24_TRUE;
        if (vncDTO.getCor() != null) {
            switch (vncDTO.getCor()) {
                case "8-bit Indexed Color": depth = ColorDepth.BPP_8_INDEXED; break;
                case "8-bit True Color":    depth = ColorDepth.BPP_8_TRUE;    break;
                case "16-bit True Color":   depth = ColorDepth.BPP_16_TRUE;   break;
                case "24-bit True Color":   depth = ColorDepth.BPP_24_TRUE;   break;
            }
        }
        config.setColorDepth(depth);

        vncClient = new VernacularClient(config);
        config.setScreenUpdateListener(this::updateVNCFrame);

        logger.info("Cliente VNC inicializado com sucesso.");
    }

    // desenha 1:1 ou escalado com interpolação
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (lastFrame == null) {
            String msg = "Sem conexão com o servidor VNC";
            int w = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, Math.max(8, (getWidth()-w)/2), Math.max(16, getHeight()/2));
            return;
        }

        if (scaleToFit) {
            // escala para caber com interpolação de qualidade
            var g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(lastFrame, 0, 0, getWidth(), getHeight(), this);
            g2.dispose();
        } else {
            // 1:1 (nítido). Painel pode ficar maior que a imagem; sem esticar.
            g.drawImage(lastFrame, 0, 0, this);
        }
    }

    // importante: para 1:1 funcionar com JScrollPane
    @Override
    public java.awt.Dimension getPreferredSize() {
        if (lastFrame != null) {
            return new java.awt.Dimension(lastFrame.getWidth(null), lastFrame.getHeight(null));
        }
        return new java.awt.Dimension(640, 480);
    }

    private void updateVNCFrame(Image frame) {
        SwingUtilities.invokeLater(() -> {
            lastFrame = frame;
            revalidate(); // atualiza barras de rolagem no 1:1
            repaint();
        });
    }

    public void connectToVNCServer(String host, int port) {
        Thread t = new Thread(() -> {
            try {
                logger.info("Conectando VNC em {}:{}...", host, port);
                vncClient.start(host, port);
                logger.info("Conectado ao servidor VNC em {}:{}.", host, port);
            } catch (Exception e) {
                logger.error("Falha ao conectar ao servidor VNC", e);
                disconnect();
            } finally {
                isConnecting.set(false);
            }
        }, "vnc-connect");
        t.setDaemon(true);
        t.start();
    }

    public void disconnect() {
        if (vncClient != null && vncClient.isRunning()) {
            vncClient.stop();
        }
        lastFrame = null;
        logger.info("Desconectado do servidor VNC.");
        SwingUtilities.invokeLater(this::repaint);
    }

    private boolean connected() {
        return vncClient != null && vncClient.isRunning();
    }

    private void manageConnection() {
        String host = vncDTO.getServidor();
        int port = Integer.parseInt(vncDTO.getPorta());

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    boolean up = isPortOpen(host, port, 3000);
                    if (up) {
                        if (!connected() && isConnecting.compareAndSet(false, true)) {
                            connectToVNCServer(host, port);
                        }
                    } else {
                        disconnect();
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "vnc-conn-mgr");
        t.setDaemon(true);
        t.start();
    }

    public static boolean isPortOpen(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void addMouseListeners() {
        // movimento
        addMouseMotionListener(new MouseMotionListener() {
            @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); }
            @Override public void mouseMoved(MouseEvent e) {
                if (!connected() || lastFrame == null) return;
                int rx, ry;
                if (scaleToFit) {
                    rx = (int) Math.round(e.getX() * (lastFrame.getWidth(null)  / (double) getWidth()));
                    ry = (int) Math.round(e.getY() * (lastFrame.getHeight(null) / (double) getHeight()));
                } else {
                    // 1:1: somar o deslocamento do viewport (scroll)
                    java.awt.Point vp = getViewPositionSafe();
                    rx = e.getX() + vp.x;
                    ry = e.getY() + vp.y;
                }
                // clamp nos limites
                rx = Math.max(0, Math.min(rx, lastFrame.getWidth(null)  - 1));
                ry = Math.max(0, Math.min(ry, lastFrame.getHeight(null) - 1));
                vncClient.moveMouse(rx, ry);
            }
        });

        // clique
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (connected()) vncClient.updateMouseButton(e.getButton(), true);
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (connected()) vncClient.updateMouseButton(e.getButton(), false);
            }
        });

        // roda do mouse
        addMouseWheelListener(e -> {
            if (!connected()) return;
            if (e.getWheelRotation() < 0) vncClient.scrollUp();
            else vncClient.scrollDown();
        });
    }

    private java.awt.Point getViewPositionSafe() {
        java.awt.Container p = getParent();
        if (p instanceof javax.swing.JViewport) {
            return ((javax.swing.JViewport) p).getViewPosition();
        }
        return new java.awt.Point(0, 0);
    }
}
