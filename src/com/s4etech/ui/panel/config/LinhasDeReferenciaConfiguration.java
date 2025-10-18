package com.s4etech.ui.panel.config;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.State;
import org.freedesktop.gstreamer.swing.GstVideoComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.s4etech.config.manager.CameraConfigurationManager;
import com.s4etech.config.manager.PerformanceConfigurationManager;
import com.s4etech.dto.CameraDTO;
import com.s4etech.dto.PerformanceDTO;
import com.s4etech.util.MessageUtils;
import com.s4etech.util.RSTPInfo;

public class LinhasDeReferenciaConfiguration extends JInternalFrame {
    /**
     *
     */
    private static final long serialVersionUID = 2886016409355896374L;

    private static final Logger logger = LoggerFactory.getLogger(LinhasDeReferenciaConfiguration.class);

    private DrawPanel drawPanel;
    private JLabel positionLabel;
    private JLabel mensagemLabel;
    private Color selectedColor = Color.YELLOW;
    private float selectedThickness = 2.0f;
    private boolean dashedLine = false;
    private boolean shiftPressed = false;
	private static final Path PATH_MARCACOES = Paths.get(System.getProperty("user.dir"), "configuracao/marcacoes/");

    private static final int VIDEO_WIDTH = 1280;
    private static final int VIDEO_HEIGHT = 720;
    private Map<String, CameraDTO> mapaCameras;
    private PerformanceDTO performanceDTO;
    private Map<String, String> mapaBasePipeline = new ConcurrentHashMap<>();
    private Map<String, GstVideoComponent> mapaVideoComponente =  new ConcurrentHashMap<>();
    private Map<String, Pipeline> mapaPipeline = new ConcurrentHashMap<>();
    private Map<String, JInternalFrame> mapaInternalFrame = new ConcurrentHashMap<>();
    private JLayeredPane layeredPane;
    private String cameraAtual = null;
    
    public LinhasDeReferenciaConfiguration() {
    	
        super("", false, false, false, false); // Remove botões padrão
        logger.info("Iniciando construtor LinhasDeReferenciaConfiguration");

        setBorder(null); // Remove a borda
        setResizable(false); // Impede redimensionamento
        setSize(VIDEO_WIDTH, VIDEO_HEIGHT);
        // Remover a barra de título do JInternalFrame
        BasicInternalFrameUI ui = (BasicInternalFrameUI) getUI();
        if (ui != null && ui.getNorthPane() != null) {
            ui.getNorthPane().setPreferredSize(new Dimension(0, 0)); // Define altura 0
            remove(ui.getNorthPane()); // Remove o painel superior
        }

        // Criar painel de desenho transparente
        drawPanel = new DrawPanel();
        drawPanel.setOpaque(false);
        drawPanel.setPreferredSize(new Dimension(VIDEO_WIDTH, VIDEO_HEIGHT));
        drawPanel.setSize(VIDEO_WIDTH, VIDEO_HEIGHT);

        // Criar painel de sobreposição
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(VIDEO_WIDTH, VIDEO_HEIGHT));
        layeredPane.setSize(VIDEO_WIDTH, VIDEO_HEIGHT);
        layeredPane.add(drawPanel, JLayeredPane.PALETTE_LAYER);

        // Criar rótulo de posição do mouse
        positionLabel = new JLabel("Posição: (0,0)");
        positionLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Adicionar componentes à janela
        add(layeredPane, BorderLayout.CENTER);
        add(createControls(), BorderLayout.SOUTH);
        add(positionLabel, BorderLayout.NORTH);

        performanceDTO = PerformanceConfigurationManager.get();
        logger.info("performanceDTO carregado: {}", performanceDTO);

        setFocusable(true);
        requestFocusInWindow();

        logger.info("Finalizando construtor LinhasDeReferenciaConfiguration");
    }

    private void startCamera(CameraDTO camera) {
        logger.info("Iniciando startCamera para código: {}", camera.getCodigo());

        RSTPInfo rstpInfo = new RSTPInfo();
        rstpInfo.setIp(camera.getIp());
        rstpInfo.setPorta(camera.getPorta());
        rstpInfo.setExtensao(camera.getExtensao());
        rstpInfo.setUsuario(camera.getUsuario());
        rstpInfo.setSenha(camera.getSenha());
        String url = rstpInfo.montarURL();
        logger.debug("URL RTSP montada: {}", url);

        String videoCodec = camera.getVideo().equals("H.264") ? "h264" : "h265";
        logger.debug("Codec de vídeo definido: {}", videoCodec);

        String basePipeline =
              "rtspsrc location=rtsp://" + url +
              " protocols=" + performanceDTO.getProtocolo() +
              " latency=" + performanceDTO.getLatency() +
              " name=" + camera.getCodigo() + "source " +
              camera.getCodigo() + "source. ! " +
              ("udp".equals(performanceDTO.getProtocolo())
                    ? "rtpjitterbuffer latency=" + performanceDTO.getLatency() + " ! "
                    : "") +
              "queue max-size-buffers=" + performanceDTO.getBufferSize() + " ! " +
              "rtp" + videoCodec + "depay ! " +
              "queue ! " + videoCodec + "parse ! " +
              (("hardware".equals(performanceDTO.getAceleracao()))
                  ? "queue ! d3d11" + videoCodec + "dec ! "
                  : "queue ! avdec_" + videoCodec + " ! ") +
              "queue ! identity silent=true ! " +
              "watchdog timeout=" + performanceDTO.getTimeout() + " ! " +
              "videoscale ! video/x-raw, width=1280, height=720 ! " +
              "queue ! videoconvert name=" + camera.getCodigo();

        mapaBasePipeline.put(camera.getCodigo(), basePipeline);
        Bin bin = (Bin) Gst.parseLaunch(mapaBasePipeline.get(camera.getCodigo()));

        mapaVideoComponente.put(camera.getCodigo(), new GstVideoComponent());
        mapaVideoComponente.get(camera.getCodigo()).setPreferredSize(new Dimension(VIDEO_WIDTH, VIDEO_HEIGHT));
        mapaVideoComponente.get(camera.getCodigo()).setSize(VIDEO_WIDTH, VIDEO_HEIGHT);

        mapaPipeline.put(camera.getCodigo(), new Pipeline());
        mapaPipeline.get(camera.getCodigo()).addMany(bin, mapaVideoComponente.get(camera.getCodigo()).getElement());
        Element.linkMany(bin, mapaVideoComponente.get(camera.getCodigo()).getElement());

        Element videoSink = mapaVideoComponente.get(camera.getCodigo()).getElement();
        videoSink.set("sync", false);
        mapaPipeline.get(camera.getCodigo()).add(videoSink);
        mapaPipeline.get(camera.getCodigo()).getElementByName(camera.getCodigo()).link(videoSink);

        mapaPipeline.get(camera.getCodigo()).setState(State.PAUSED);
        mapaPipeline.get(camera.getCodigo()).play();
        logger.debug("Pipeline {} iniciado (PAUSED -> PLAY)", camera.getCodigo());

        mapaInternalFrame.put(camera.getCodigo(), new JInternalFrame());
        JInternalFrame internalFrame = mapaInternalFrame.get(camera.getCodigo());

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
              (javax.swing.plaf.basic.BasicInternalFrameUI) internalFrame.getUI();
        ui.setNorthPane(null);

        internalFrame.setBorder(BorderFactory.createLineBorder(Color.BLACK, -1));
        internalFrame.setFrameIcon(new ImageIcon(
              new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE)
        ));

        internalFrame.getContentPane().setLayout(new BorderLayout());
        internalFrame.getContentPane().add(mapaVideoComponente.get(camera.getCodigo()), BorderLayout.CENTER);
        internalFrame.setSize(VIDEO_WIDTH, VIDEO_HEIGHT);
        internalFrame.getContentPane().revalidate();
        internalFrame.getContentPane().repaint();
        internalFrame.setVisible(true);

        layeredPane.add(internalFrame, JLayeredPane.DEFAULT_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
        layeredPane.setVisible(true);

        logger.info("startCamera finalizado para câmera: {}", camera.getCodigo());
    }

    private Map<String, CameraDTO> mapearCamerasPorCodigo(List<CameraDTO> cameras) {
        logger.info("Mapeando lista de {} câmeras para Map<código,CameraDTO>", cameras.size());
        return cameras.stream()
                .collect(Collectors.toMap(CameraDTO::getCodigo, camera -> camera));
    }

    private JPanel createControls() {
        logger.info("Iniciando createControls()");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
     
        List<CameraDTO> cameras = CameraConfigurationManager.get();

	     // Ordena
	     cameras.sort(Comparator.comparing(CameraDTO::getCodigo, Comparator.nullsLast(String::compareTo)));
	
	     mapaCameras = mapearCamerasPorCodigo(cameras);
	
	     // Agora preenche o ComboBox a partir da lista ordenada
	     String[] codigosCameras = new String[cameras.size() + 1];
	     codigosCameras[0] = "Selecione...";
	     int i = 1;
	     for (CameraDTO cam : cameras) {
	         codigosCameras[i++] = cam.getCodigo();
	     }
	     
	     JComboBox<String> cameraBox = new JComboBox<>(codigosCameras);

        
        cameraBox.setPreferredSize(new Dimension(120, 25));
        cameraBox.addActionListener(e -> {
            String selectedCamera = (String) cameraBox.getSelectedItem();
            logger.debug("cameraBox action: {}", selectedCamera);
            if (selectedCamera == null || "selecione".equals(selectedCamera)) {
                logger.debug("cameraBox selecionou valor nulo ou 'selecione', ignorando...");
                return;
            }
            if (selectedCamera.equals(cameraAtual)) {
                logger.debug("Tentativa de reabrir a mesma câmera, ignorando...");
                return;
            }
            if (cameraAtual != null && mapaCameras.containsKey(cameraAtual)) {
                logger.info("Fechando câmera atual antes de abrir nova: {}", cameraAtual);
                closeCamera(mapaCameras.get(cameraAtual));
            }
            cameraAtual = selectedCamera;
            logger.info("Abrindo câmera: {}", cameraAtual);
            startCamera(mapaCameras.get(selectedCamera));
            
            // Adiciona a limpeza antes de carregar novas marcações
            drawPanel.clear(); // 🔹 Limpa as linhas anteriores
            logger.info("Linhas limpas antes de carregar novas marcações.");

            // Carrega as marcações específicas da nova câmera
            String drawingFile = mapaCameras.get(selectedCamera).getCodigo().concat(".json");
            logger.info("Carregando arquivo de marcações: {}", drawingFile);
            drawPanel.loadFromFile(drawingFile);
        });

        String[] colors = {"Amarelo", "Azul", "Verde", "Vermelho"};
        JComboBox<String> colorBox = new JComboBox<>(colors);
        colorBox.setPreferredSize(new Dimension(100, 25));
        colorBox.addActionListener(e -> {
            String sel = (String) colorBox.getSelectedItem();
            logger.debug("Cor selecionada: {}", sel);
            switch (sel) {
            	case "Amarelo"  -> selectedColor = Color.YELLOW;
            	case "Azul"     -> selectedColor = Color.CYAN;
                case "Verde"    -> selectedColor = Color.GREEN;
                case "Vermelho" -> selectedColor = Color.RED;
            }
        });

        String[] thicknesses = {"1px", "3px", "5px"};
        JComboBox<String> thicknessBox = new JComboBox<>(thicknesses);
        thicknessBox.setPreferredSize(new Dimension(80, 25));
        thicknessBox.addActionListener(e -> {
            String sel = thicknessBox.getSelectedItem().toString();
            logger.debug("Espessura selecionada: {}", sel);
            selectedThickness = Float.parseFloat(sel.replace("px", ""));
        });

        JCheckBox dashedCheck = new JCheckBox("Pontilhada");
        dashedCheck.addActionListener(e -> {
            dashedLine = dashedCheck.isSelected();
            logger.debug("Linhas pontilhadas: {}", dashedLine);
        });

        JButton undoButton = new JButton("Desfazer");
        undoButton.addActionListener(e -> {
            logger.info("Botão 'Desfazer' acionado");
            drawPanel.undoLastLine();
        });

        JButton clearButton = new JButton("Limpar");
        clearButton.addActionListener(e -> {
            logger.info("Botão 'Limpar' acionado");
            drawPanel.clear();
        });

        JButton saveButton = new JButton("Salvar");
        saveButton.addActionListener(e -> {
        		drawPanel.saveToFile(cameraBox.getItemAt(cameraBox.getSelectedIndex()).concat(".json"));
        });

        centerPanel.add(new JLabel("Câmera:"));
        centerPanel.add(cameraBox);
        centerPanel.add(new JLabel("Cor:"));
        centerPanel.add(colorBox);
        centerPanel.add(new JLabel("Espessura:"));
        centerPanel.add(thicknessBox);
        centerPanel.add(dashedCheck);
        centerPanel.add(undoButton);
        centerPanel.add(clearButton);
        centerPanel.add(saveButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        rightPanel.setPreferredSize(new Dimension(200, 30));

        mensagemLabel = new JLabel();
        rightPanel.add(mensagemLabel);

        mainPanel.add(rightPanel, BorderLayout.EAST);

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(200, 1));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        logger.info("createControls() concluído");
        return mainPanel;
    }

    private void closeCamera(CameraDTO camera) {
        try {
            logger.info("Fechando câmera: {}", camera.getCodigo());

            JInternalFrame frame = mapaInternalFrame.remove(camera.getCodigo());
            if (frame != null) {
                layeredPane.remove(frame);
                frame.dispose();
            }

            Pipeline pipeline = mapaPipeline.remove(camera.getCodigo());
            if (pipeline != null) {
                pipeline.setState(State.NULL);

                Bus bus = pipeline.getBus();
                if (bus != null) {
                    bus.dispose();
                }
                pipeline.dispose();
            }

            mapaVideoComponente.remove(camera.getCodigo());
            layeredPane.revalidate();
            layeredPane.repaint();

            logger.info("Câmera {} foi fechada com sucesso.", camera.getCodigo());

        } catch (Exception e) {
            logger.error("Erro ao fechar a câmera {}: ", camera.getCodigo(), e);
        }
    }

    class DrawPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private List<Line> lines = new ArrayList<>();
        private Point startPoint = null;
        private Point currentMousePoint = null;

        public DrawPanel() {
            logger.info("Iniciando construtor DrawPanel");
            setPreferredSize(new Dimension(VIDEO_WIDTH, VIDEO_HEIGHT));
            setSize(VIDEO_WIDTH, VIDEO_HEIGHT);

            logger.info("Construtor DrawPanel finalizado, sem carregar arquivo inicialmente.");

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    logger.debug("Mouse pressed em ({},{})", e.getX(), e.getY());
                    startPoint = e.getPoint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    logger.debug("Mouse released em ({},{})", e.getX(), e.getY());
                    if (startPoint != null) {
                        int x2 = e.getX();
                        int y2 = e.getY();

                        if (shiftPressed) {
                            if (Math.abs(x2 - startPoint.x) > Math.abs(y2 - startPoint.y)) {
                                y2 = startPoint.y;
                            } else {
                                x2 = startPoint.x;
                            }
                        }
                        lines.add(new Line(startPoint.x, startPoint.y, x2, y2, selectedColor, selectedThickness, dashedLine));
                        logger.debug("Linha adicionada: ({},{}) -> ({},{})", startPoint.x, startPoint.y, x2, y2);

                        startPoint = null;
                        currentMousePoint = null;
                        repaint();
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    positionLabel.setText("Posição: (" + e.getX() + "," + e.getY() + ")");
                    currentMousePoint = e.getPoint();
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    positionLabel.setText("Posição: (" + e.getX() + "," + e.getY() + ")");
                    currentMousePoint = e.getPoint();
                    repaint();
                }
            });
        }

        public void loadFromFile(String filename) {
            logger.info("Carregando arquivo de linhas: {}", filename);
            try {
                File file = PATH_MARCACOES.resolve(filename).toFile(); 
                if (!file.exists()) {
                    logger.debug("Arquivo {} não existe, não há linhas para carregar.", file.getAbsolutePath());
                    return;
                }

                Gson gson = new Gson();
                try (Reader reader = new FileReader(file)) {
                    lines = gson.fromJson(reader, new TypeToken<List<Line>>() {}.getType());
                }
                logger.info("Arquivo {} carregado com sucesso. Total de linhas: {}", file.getAbsolutePath(), lines.size());

                repaint();
            } catch (IOException e) {
                logger.error("Erro ao carregar o arquivo {}", filename, e);

                Thread animationThread = createAnimationThread("Erro ao carregar o arquivo!", Color.RED);
                animationThread.start();
            }
        }


        public void clear() {
            logger.info("Limpando todas as linhas desenhadas.");
            lines.clear();
            repaint();
        }

        public void undoLastLine() {
            if (!lines.isEmpty()) {
                logger.debug("Removendo última linha adicionada.");
                lines.remove(lines.size() - 1);
                repaint();
            } else {
                logger.debug("Não há linhas para remover em 'undoLastLine'.");
            }
        }

        public void saveToFile(String filename) {
            logger.info("Salvando desenho no arquivo: {}", filename);
            try {
                File directory = PATH_MARCACOES.toFile(); 
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File file = PATH_MARCACOES.resolve(filename).toFile(); 
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonContent = gson.toJson(lines);

                try (Writer writer = new FileWriter(file)) {
                    writer.write(jsonContent);
                }

                logger.info("Arquivo salvo com sucesso em: {}", file.getAbsolutePath());
                Thread animationThread = createAnimationThread("Marcações salvas com sucesso.", Color.WHITE);
                animationThread.start();

            } catch (IOException e) {
                logger.error("Erro ao salvar o arquivo {}", filename, e);

                Thread animationThread = createAnimationThread("Erro ao salvar o arquivo.", Color.RED);
                animationThread.start();
            }
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Line line : lines) {
                g2.setColor(line.getColor());
                g2.setStroke(line.getStroke());
                g2.drawLine(line.x1, line.y1, line.x2, line.y2);
            }

            // Desenha a linha guia
            if (startPoint != null && currentMousePoint != null) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(startPoint.x, startPoint.y, currentMousePoint.x, currentMousePoint.y);
            }
        }

        private Thread createAnimationThread(String mensagem, Color corDaMensagem) {
            logger.debug("Criando animationThread para mensagem: {}", mensagem);
            return new Thread(() -> {
                int count = 0;
                while (!Thread.currentThread().isInterrupted() && count < 3) {
                    SwingUtilities.invokeLater(() -> {
                        MessageUtils.updateDescriptionLabel(mensagemLabel, mensagem, corDaMensagem);
                    });

                    count++;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    mensagemLabel.setText("");
                });
                logger.debug("animationThread finalizado para mensagem: {}", mensagem);
            });
        }

    }

    class Line {
        int x1, y1, x2, y2;
        String color;
        float thickness;
        boolean dashed;

        public Line(int x1, int y1, int x2, int y2, Color color, float thickness, boolean dashed) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = getColorName(color);
            this.thickness = thickness;
            this.dashed = dashed;
            logger.debug("Criada Line: {}", this.toString());
        }

        private String getColorName(Color color) {
            if (color.equals(Color.RED)) return "Vermelho";
            if (color.equals(Color.GREEN)) return "Verde";
            if (color.equals(Color.YELLOW)) return "Amarelo";
            if (color.equals(Color.CYAN)) return "Azul";
            return "Desconhecido";
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
                ? new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                  10, new float[]{10, 10}, 0)
                : new BasicStroke(thickness);
        }

        @Override
        public String toString() {
            return "Line{x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2
                    + ", color='" + color + "', thickness=" + thickness + ", dashed=" + dashed + "}";
        }
    }
}
