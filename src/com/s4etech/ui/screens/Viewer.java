package com.s4etech.ui.screens;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.xml.soap.SOAPException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.PadProbeReturn;
import org.freedesktop.gstreamer.PadProbeType;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.State;
import org.freedesktop.gstreamer.event.EOSEvent;
import org.onvif.ver10.schema.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.s4etech.config.manager.AutorizacaoConfigurationManager;
import com.s4etech.config.manager.CameraConfigurationManager;
import com.s4etech.config.manager.CenarioFileManager;
import com.s4etech.config.manager.GravacaoConfigurationManager;
import com.s4etech.config.manager.PerformanceConfigurationManager;
import com.s4etech.config.manager.UDPConfigurationManager;
import com.s4etech.config.manager.VNCConfigurationManager;
import com.s4etech.config.manager.WebBrowserConfigurationManager;
import com.s4etech.dto.AutorizacaoDTO;
import com.s4etech.dto.CameraDTO;
import com.s4etech.dto.CenarioDTO;
import com.s4etech.dto.ConfiguracaoVisualDTO;
import com.s4etech.dto.ConfiguracaoVisualPtzDTO;
import com.s4etech.dto.GravacaoDTO;
import com.s4etech.dto.PerformanceDTO;
import com.s4etech.dto.UDPDTO;
import com.s4etech.dto.VNCDTO;
import com.s4etech.dto.WebBrowserDTO;
import com.s4etech.integration.AlertDisplay;
import com.s4etech.integration.ScreenRecorder;
import com.s4etech.integration.VNCPanel;
import com.s4etech.integration.VideoOverlayManager;
import com.s4etech.ui.panel.config.AutorizacaoConfigurationPanel;
import com.s4etech.ui.panel.config.CameraConfigurationPanel;
import com.s4etech.ui.panel.config.CenarioConfigurationPanel;
import com.s4etech.ui.panel.config.EstadoConfigurationPanel;
import com.s4etech.ui.panel.config.GravacaoConfigurationPanel;
import com.s4etech.ui.panel.config.LinhasDeReferenciaConfiguration;
import com.s4etech.ui.panel.config.PerformanceConfigurationPanel;
import com.s4etech.ui.panel.config.SobreConfigurationPanel;
import com.s4etech.ui.panel.config.UDPConfigurationPanel;
import com.s4etech.ui.panel.config.VNCConfigurationPanel;
import com.s4etech.ui.panel.config.WebBrowserConfigurationPanel;
import com.s4etech.util.AlarmSoundHandler;
import com.s4etech.util.CustomRoundedPanel;
import com.s4etech.util.FileScenarioChecker;
import com.s4etech.util.GstVideoComponent;
import com.s4etech.util.MenuItem;
import com.s4etech.util.MonitorInfo;
import com.s4etech.util.PTZOverlayPanel;
import com.s4etech.util.PipelineUtils;
import com.s4etech.util.RSTPInfo;
import com.s4etech.util.RotationOverlayPanel;
import com.s4etech.util.StatusCamera;
import com.s4etech.util.WatermarkManager;

import de.onvif.soap.OnvifDevice;
import de.onvif.soap.devices.PtzDevices;

public class Viewer implements Serializable {

	// =========================================
	// 0. SERIALIZAÇÃO E LOGGER
	// =========================================
	private static final long serialVersionUID = -5983049372188366126L;
	private static final Logger logger = LoggerFactory.getLogger(Viewer.class);

	// =========================================
	// 1. ESTADOS GERAIS
	// =========================================
	private boolean isMenuExpanded = false;
	private boolean isGridFreeShow = false;
	private boolean isBladeFixed = false;
	private boolean isAroundFixed = false;
	private boolean isRipperFixed = false;
	private boolean isAutorizado = false;
	private int contadorThreadsIniciadas = 0;
	private int heartbeatCounter = 0;
	private String resizeDirection = null;
	private static String usuario;

	// =========================================
	// 2. CONTROLE DE INTERAÇÃO DO USUÁRIO
	// =========================================
	private Point clickPoint;

	// =========================================
	// 3. COMPONENTES DA UI (SWING)
	// =========================================
	private JLabel udpLabel, gravacaoLabel, gridFreeLabel;
	private JButton udpButton, gravacaoButton;
	private JButton memorizeButton, ptzButton;
	private JPopupMenu ptzMenu;
	private JInternalFrame menuInternalFrame;
	private JPanel gridFreePanel, gridFreeTextPanel;
	private JLayeredPane layeredPane;
	private JMenu configuracaoMenu;
	private Font menuFont;
	private final Object lock = new Object();

	// =========================================
	// 4. THREADS & EXECUTORES
	// =========================================
	private ThreadPoolExecutor executor;
	private Thread animationThread;
	private ExecutorService webViewExecutor = Executors.newFixedThreadPool(2);
	private ExecutorService vncExecutor = Executors.newFixedThreadPool(1);
	private ExecutorService gravacaoExecutor = Executors.newFixedThreadPool(1);
	private final ExecutorService cameraStatusExecutor = Executors.newFixedThreadPool(1);
	private volatile boolean runningCameraStatusExecutor = true;
	private Thread udpStatusThread;
	private Thread udpAlarmeThread;
	private Thread ackMonitorThread;
	private java.util.Timer timerMonitoramento;

	// =========================================
	// 5. ARQUIVOS & CONFIGURAÇÕES
	// =========================================
	private Path pathvisualconf, pathvisualptzconf, pathscenariovisualconf, pathcenariovisualptzconf;
	private final int RESIZE_MARGIN = 10;
	private static final AlarmSoundHandler alarmHandler = new AlarmSoundHandler();

	// =========================================
	// 6. OBJETOS DE CONTROLE & DTOs
	// =========================================
	private PerformanceDTO performanceDTO = null;
	private ScreenRecorder screenRecorder = null;
	private UDPDTO udpDTO = null;
	private AtomicBoolean runningUDP = new AtomicBoolean(true);
	private List<ConfiguracaoVisualDTO> configuracaoVisual;
	private List<ConfiguracaoVisualPtzDTO> camerasEstadoZero = null;
	private List<CameraDTO> listaParaStatusCamera = null;

	// =========================================
	// 7. MAPAS DE CONTROLE E ASSOCIAÇÃO
	// =========================================
	private Map<JButton, Boolean> buttonStates = new HashMap<>();
	private Map<String, CameraDTO> mapaCameras;
	private Map<String, ConfiguracaoVisualDTO> mapaConfiguracaoVisualCameras;
	private Map<String, Pipeline> mapaPipeline = new ConcurrentHashMap<>();
	private Map<String, GstVideoComponent> mapaVideoComponente = new ConcurrentHashMap<>();
	private Map<String, JInternalFrame> mapaInternalFrame = new ConcurrentHashMap<>();
	private Map<String, JPanel> mapaVideoPanel = new ConcurrentHashMap<>();
	private Map<String, PTZOverlayPanel> mapaPTZOverlayPanel = new ConcurrentHashMap<>();
	private Map<String, RotationOverlayPanel> mapaRotationOverlayPanel = new ConcurrentHashMap<>();
	private Map<String, Point> mapaLastLocation = new ConcurrentHashMap<>();
	private Map<String, OnvifDevice> mapaOnvifDevice = new ConcurrentHashMap<>();
	private Map<String, Pad> mapaPad = new ConcurrentHashMap<>();
	private Map<String, String> mapaBasePipeline = new ConcurrentHashMap<>();
	private Map<String, Timer> mapaReconnectTimers = new ConcurrentHashMap<>();
	private Map<String, Boolean> mapaIsReconnecting = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Double> mapaFps = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Long> mapaLastPacketTime = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Long> mapaLatenciaPacote = new ConcurrentHashMap<>();
	private final Map<String, List<Long>> mapaLatenciasPacote = new ConcurrentHashMap<>();
	private Map<String, Long> mapaStartTime = new ConcurrentHashMap<>();
	private Map<String, Integer> mapaFrameCount = new ConcurrentHashMap<>();
	private final Map<String, JInternalFrame> mapaWebViewFrames = new ConcurrentHashMap<>();
	private final Map<String, JInternalFrame> mapaVNCFrames = new ConcurrentHashMap<>();
    private final Map<String, Browser> mapaBrowser = new HashMap<>();
	private final Map<String, Bus.ERROR> mapaBusErrorHandlers = new HashMap<>();
	private final Map<String, Bus.STATE_CHANGED> mapaBusStateHandlers = new HashMap<>();
	private final Map<String, Bus.ERROR> mapaBusReconnectHandlers = new HashMap<>();
	private final Map<Long, Long> acksPendentes = Collections.synchronizedMap(new LinkedHashMap<>());

	// =========================================
	// 8. LISTAS
	// =========================================
	private List<JButton> buttons = new ArrayList<>();
	private List<JLabel> labels = new ArrayList<>();

	// =========================================
	// 9. ÍCONES
	// =========================================
	private static final ImageIcon ICON_APP_LOGO = new ImageIcon(Viewer.class.getResource("/icons/icone_s4etech.png"));
	private static final ImageIcon ICON_CENARIO_OFF = new ImageIcon(Viewer.class.getResource("/icons/cenario_off.png"));
	private static final ImageIcon ICON_CENARIO_ON = new ImageIcon(Viewer.class.getResource("/icons/cenario_on.png"));
	private static final ImageIcon ICON_CENARIO_HOVER = new ImageIcon(
			Viewer.class.getResource("/icons/cenario_on.png"));
	private static final ImageIcon ICON_CONFIGURACAO = new ImageIcon(
			Viewer.class.getResource("/icons/configuracao.png"));
	private static final ImageIcon ICON_CONFIGURACAO_HOVER = new ImageIcon(
			Viewer.class.getResource("/icons/configuracao_hover.png"));
	private static final ImageIcon ICON_FECHAR = new ImageIcon(Viewer.class.getResource("/icons/fechar.png"));
	private static final ImageIcon ICON_FIXAR_OFF = new ImageIcon(Viewer.class.getResource("/icons/fixar_off.png"));
	private static final ImageIcon ICON_FIXAR_ON = new ImageIcon(Viewer.class.getResource("/icons/fixar_on.png"));
	private static final ImageIcon ICON_GRAVACAO_OFF = new ImageIcon(
			Viewer.class.getResource("/icons/gravacao_off.png"));
	private static final ImageIcon ICON_GRAVACAO_ON_GREEN = new ImageIcon(
			Viewer.class.getResource("/icons/gravacao_on_green.png"));
	private static final ImageIcon ICON_GRID_FREE = new ImageIcon(Viewer.class.getResource("/icons/grid_free.png"));
	private static final ImageIcon ICON_GRID_FREE_GREEN = new ImageIcon(
			Viewer.class.getResource("/icons/grid_free_green.png"));
	private static final ImageIcon ICON_GRID_FREE_HOVER = new ImageIcon(
			Viewer.class.getResource("/icons/grid_free_hover.png"));
	private static final ImageIcon ICON_PCAN = new ImageIcon(Viewer.class.getResource("/icons/pcan.png"));
	private static final ImageIcon ICON_PCAN_DESCONECTADA = new ImageIcon(
			Viewer.class.getResource("/icons/pcan_desconectada.png"));
	private static final ImageIcon ICON_PCAN_OPERACIONAL = new ImageIcon(
			Viewer.class.getResource("/icons/pcan_operacional.png"));
	private static final ImageIcon ICON_PTZ_OFF = new ImageIcon(Viewer.class.getResource("/icons/ptz_off.png"));
	private static final ImageIcon ICON_PTZ_ON = new ImageIcon(Viewer.class.getResource("/icons/ptz_on.png"));
	private static final ImageIcon ICON_SAIR = new ImageIcon(Viewer.class.getResource("/icons/sair.png"));
	private static final ImageIcon ICON_SAIR_HOVER = new ImageIcon(Viewer.class.getResource("/icons/sair_hover.png"));
	private static final ImageIcon ICON_VAZIO = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE));

	// =========================================
	// 10. CONTROLE DE GRAVAÇÃO
	// =========================================
	private final Object gravacaoLock = new Object();
	private boolean gravacaoEmCriacao = false;

	// =========================================
	// 11. SOCKET UDP
	// =========================================
	private DatagramSocket udpSocket;
	
	private final Map<String, Boolean> scenarioFixed = new LinkedHashMap<>();
	private final java.util.List<JPanel> scenarioPanels = new java.util.ArrayList<>();
	private CustomRoundedPanel sideMenuPanel;
	private JSeparator separator2;
	private JSeparator separator3;
	
	private final Map<String, java.util.concurrent.atomic.AtomicBoolean> reconnecting = new java.util.concurrent.ConcurrentHashMap<>();
	private final Map<String, Integer> retryAttempts = new java.util.concurrent.ConcurrentHashMap<>();
	private final Map<String, Long>    nextRetryAt   = new java.util.concurrent.ConcurrentHashMap<>();
	private static final long BASE_DELAY_MS = 2000;   // 2s
	private static final long MAX_DELAY_MS  = 60000;  // 60s

	private int selectedIndex = -1; // guarda o botão ativo
	
	private static final int TOTAL_CENARIOS = 7;

	public Viewer(AutorizacaoDTO autorizacaoDTO) {

		usuario = autorizacaoDTO.getUsuario();
	}

	public void inicializa() {

		Gst.init("Viewer");

		logger.info("Inicializando o sistema de visualização.");

		try {

			pathvisualconf = Paths.get(System.getProperty("user.dir"), "configuracao/" + usuario + "_visual.cfg");
			pathvisualptzconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/" + usuario + "_visual_ptz.cfg");

			pathscenariovisualconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_visual.cfg");
			pathcenariovisualptzconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_visual_ptz.cfg");

			Map<String, Path> listaCenarios =  FileScenarioChecker.listarCenarios(usuario);

			GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Rectangle result = new Rectangle();
			for (GraphicsDevice gd : localGE.getScreenDevices()) {
				for (GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) {
					result = result.union(graphicsConfiguration.getBounds());
				}
			}

			int taskBarHeight = getTaskBarHeight();

			result.height -= taskBarHeight;

			JFrame frame = new JFrame("S4ETech - Viewer");
			frame.setIconImage(ICON_APP_LOGO.getImage());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setUndecorated(true);
			frame.setOpacity(1f);
			frame.setSize(result.width, result.height);
			frame.setLocation(result.x, result.y);
			frame.setBounds(result);

			layeredPane = new JLayeredPane();
			layeredPane.setLayout(null); // 🔧 permite mover componentes livremente

			sideMenuPanel = new CustomRoundedPanel(20, 20);
			sideMenuPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));

			// Define posição inicial e tamanho fixo
			sideMenuPanel.setBounds(result.width - 130, 20, 130, 390); // posição inicial
			layeredPane.add(sideMenuPanel, JLayeredPane.POPUP_LAYER);

			// Cria o JWindow com o menu como conteúdo
			JWindow menuWindow = new JWindow(frame); // ou null, se não quiser associado ao JFrame
			menuWindow.setBackground(new Color(0, 0, 0, 0)); // completamente transparente
			menuWindow.getContentPane().add(sideMenuPanel);
			//menuWindow.setSize(130, 530);

			SwingUtilities.invokeLater(() -> {
				Point frameLocation = frame.getLocationOnScreen();
				menuWindow.setLocation(frameLocation.x + frame.getWidth() - 130, frameLocation.y + 20);
				// Exibe o menu flutuante sempre por cima
				menuWindow.setAlwaysOnTop(true);
				//menuWindow.setVisible(true);
			});

			// Criar uma instância do WatermarkManager e adicionar os painéis de marca
			// d'água
			WatermarkManager watermarkManager = new WatermarkManager(layeredPane);
			watermarkManager.addWatermarkPanels();

			frame.setContentPane(layeredPane);

			JPanel closeButtonPanel = new JPanel();
			closeButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Define o layout do botão close
			closeButtonPanel.setOpaque(false);

			JButton closeButton = new JButton();
			closeButton.setIcon(ICON_FECHAR);
			closeButton.setPreferredSize(new Dimension(16, 16)); // Define o tamanho do botão, se necessário
			closeButton.setBorder(BorderFactory.createEmptyBorder());
			closeButton.setContentAreaFilled(false);
			
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final int DELAY = 10;
					final int STEP = isMenuExpanded ? -4 : 4;
					final int TARGET = sideMenuPanel.getX() + (isMenuExpanded ? -85 : 85);
					Timer timer = new Timer(DELAY, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int x = sideMenuPanel.getX() + STEP;

							if ((STEP > 0 && x >= TARGET) || (STEP < 0 && x <= TARGET)) {
								x = TARGET;
								((Timer) e.getSource()).stop();

								closeButton.setIcon(new ImageIcon(Viewer.class
										.getResource(isMenuExpanded ? "/icons/fechar.png" : "/icons/abrir.png")));

								isMenuExpanded = !isMenuExpanded;
							}
							sideMenuPanel.setLocation(x, sideMenuPanel.getY());
						}
					});
					timer.start();
				}
			});

			closeButtonPanel.add(closeButton);

			JPanel menuTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
			menuTextPanel.setOpaque(false);

			JLabel menuLabel = new JLabel("Configurações");
			menuLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
			menuLabel.setForeground(Color.GRAY);
			menuLabel.setIconTextGap(10);
			menuLabel.setIcon(ICON_CONFIGURACAO_HOVER);

			menuTextPanel.add(menuLabel);

			JPanel menuPanel = new JPanel();
			menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

			menuPanel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {

					setAutorizado(false);

					if (menuInternalFrame != null && menuInternalFrame.isVisible()) {
						// Traz a janela existente para a frente
						try {
							menuInternalFrame.setSelected(true);
						} catch (java.beans.PropertyVetoException ex) {
							ex.printStackTrace();
						}
						return;
					}

					// Adicionar a verificação de isGridFreeShow
					if (isGridFreeShow) {
						toggleGridFree();
					}

					menuInternalFrame = new JInternalFrame(null, true, false, false, false) {

						private static final long serialVersionUID = -6483431129812583456L;

						@Override
						public void updateUI() {
							super.updateUI();
							BasicInternalFrameUI ui = (BasicInternalFrameUI) getUI();
							if (ui.getNorthPane() != null) {
								ui.getNorthPane().setPreferredSize(new Dimension(getWidth(), 0));
							}
						}
					};

					menuInternalFrame.toFront();
					menuInternalFrame.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							clickPoint = e.getPoint();
						}
					});

					menuInternalFrame.addMouseMotionListener(new MouseMotionAdapter() {
						@Override
						public void mouseDragged(MouseEvent e) {
							Point dragPoint = e.getPoint();
							int dx = dragPoint.x - clickPoint.x;
							int dy = dragPoint.y - clickPoint.y;
							Point frameLocation = menuInternalFrame.getLocation();
							menuInternalFrame.setLocation(frameLocation.x + dx, frameLocation.y + dy);
						}
					});

					menuInternalFrame.setSize(600, 540);
					menuInternalFrame.setVisible(true);
					menuInternalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

					// ---

					JMenu exitMenu = new JMenu("Sair");
					exitMenu.setFont(new Font("Roboto", Font.PLAIN, 12));

					// Adiciona o evento de clique diretamente ao menu
					exitMenu.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (menuInternalFrame != null) {
								menuInternalFrame.setVisible(false);
							}
						}
					});

					exitMenu.addActionListener(event -> {
						if (menuInternalFrame != null) {
							menuInternalFrame.getContentPane().removeAll(); // Limpa os componentes
							menuInternalFrame.repaint(); // Revalida a interface
							menuInternalFrame.setVisible(false); // Oculta a janela
						}
					});

					JMenuBar menuBar = new JMenuBar();
					menuBar.add(createMenuBar());
					menuBar.add(createHelpBar());
					menuBar.add(exitMenu);
					menuBar.add(Box.createHorizontalGlue());
					menuBar.add(autorizacaoMenuBar());

					menuInternalFrame.add(new EstadoConfigurationPanel());
					menuInternalFrame.setJMenuBar(menuBar);

					layeredPane.add(menuInternalFrame, JLayeredPane.POPUP_LAYER);

					try {
						menuInternalFrame.setSelected(false);
					} catch (java.beans.PropertyVetoException ex) {
						ex.printStackTrace();
					}

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					menuLabel.setIcon(ICON_CONFIGURACAO);
					menuLabel.setForeground(Color.WHITE); // Mudar a cor do texto para cinza quando o mouse entra
				}

				@Override
				public void mouseExited(MouseEvent e) {
					menuLabel.setIcon(ICON_CONFIGURACAO_HOVER);
					menuLabel.setForeground(Color.GRAY); // Voltar a cor do texto para branco quando o mouse sai
				}
			});

			menuPanel.setOpaque(false);
			menuPanel.add(menuTextPanel);

			gridFreeTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0)); // 20 pixels de espaçamento no
																					// início para o texto
			gridFreeTextPanel.setOpaque(false);

			gridFreeLabel = new JLabel();
			gridFreeLabel.setText("Abrir Grid");
			gridFreeLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
			gridFreeLabel.setForeground(Color.GRAY);
			gridFreeLabel.setIconTextGap(10);
			gridFreeLabel.setIcon(ICON_GRID_FREE_HOVER);
			gridFreeTextPanel.add(gridFreeLabel);

			gridFreePanel = new JPanel();
			gridFreePanel.setLayout(new BoxLayout(gridFreePanel, BoxLayout.X_AXIS));
			gridFreePanel.setOpaque(false);

			gridFreePanel.add(gridFreeTextPanel);

			gridFreePanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					SwingUtilities.invokeLater(() -> {
						if (!"Aguarde...".equals(gridFreeLabel.getText())) {
							toggleGridFree();
						}
					});
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					SwingUtilities.invokeLater(() -> {
						if (!"Aguarde...".equals(gridFreeLabel.getText())) {
							if (!isGridFreeShow) {
								gridFreeLabel.setIcon(ICON_GRID_FREE);
								gridFreeLabel.setForeground(Color.WHITE);
							} else {
								gridFreeLabel.setIcon(ICON_GRID_FREE_HOVER);
								gridFreeLabel.setForeground(Color.GRAY); // Ou outra cor, caso o grid esteja fechado
							}

						}
					});

				}

				@Override
				public void mouseExited(MouseEvent e) {
					SwingUtilities.invokeLater(() -> {
						if (!"Aguarde...".equals(gridFreeLabel.getText())) {
							if (isGridFreeShow) {
								gridFreeLabel.setIcon(ICON_GRID_FREE_GREEN);
								gridFreeLabel.setForeground(Color.WHITE);
							} else {
								gridFreeLabel.setIcon(ICON_GRID_FREE_HOVER);
								gridFreeLabel.setForeground(Color.GRAY); // Ou outra cor, caso o grid esteja fechado
							}
						}
					});
				}

			});

			// ----------

			JPanel memorizeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
			memorizeButtonPanel.setOpaque(false);

			if (memorizeButton == null) {
				memorizeButton = new JButton();
				memorizeButton.setIcon(ICON_FIXAR_OFF);
			}

			memorizeButton.setPreferredSize(new Dimension(20, 20));
			memorizeButton.setBorder(BorderFactory.createEmptyBorder());
			memorizeButton.setContentAreaFilled(false);

			memorizeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					if (isGridFreeShow) {

						for (String codigo : mapaInternalFrame.keySet()) { // Itera sobre todas as câmeras no mapa
							JInternalFrame frame = mapaInternalFrame.get(codigo);

							if (frame != null && frame.isVisible()) {
								String codigoCamera = codigo; // Usa o código como identificação padrão

								// Obtém posição e tamanho do frame
								int x = frame.getLocation().x;
								int y = frame.getLocation().y;
								int width = frame.getWidth();
								int height = frame.getHeight();

								// Criação do DTO para armazenar configurações
								ConfiguracaoVisualDTO configuracaoVisualDTO = new ConfiguracaoVisualDTO();
								configuracaoVisualDTO.setCodigoCamera(codigoCamera);
								configuracaoVisualDTO.setTamanhoHorizontal(width);
								configuracaoVisualDTO.setTamanhoVertical(height);
								configuracaoVisualDTO.setPosicaoHorizontal(x);
								configuracaoVisualDTO.setPosicaoVertical(y);

								configuracaoVisualDTO.setRotate("0");
								configuracaoVisualDTO.setFlip("none");

								// Configurações de rotação e espelhamento
								if (mapaBasePipeline.containsKey(codigo)) {
									String definicao = mapaBasePipeline.get(codigo);
									configuracaoVisualDTO.setRotate(PipelineUtils.getVideoDirection(definicao));
									configuracaoVisualDTO.setFlip(PipelineUtils.getMethod(definicao));
								}

								gravarArquivoConfiguracaoVisual(configuracaoVisualDTO);
							}
						}

					}
				}
			});

			memorizeButtonPanel.add(memorizeButton);

			JPanel memorizeTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
			memorizeTextPanel.setOpaque(false);

			JLabel memorizeLabel = new JLabel("Fixar Grid");
			memorizeLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
			memorizeLabel.setForeground(Color.WHITE);
			memorizeTextPanel.add(memorizeLabel);

			JPanel memorizePanel = new JPanel();
			memorizePanel.setLayout(new BoxLayout(memorizePanel, BoxLayout.X_AXIS));
			memorizePanel.setOpaque(false);

			memorizePanel.add(memorizeButtonPanel);
			memorizePanel.add(memorizeTextPanel);

			// ----------

			JPanel udpButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
			udpButtonPanel.setOpaque(false);

			udpButton = new JButton();
			udpButton.setIcon(ICON_PCAN);
			udpButton.setPreferredSize(new Dimension(21, 23));
			udpButton.setBorder(BorderFactory.createEmptyBorder());
			udpButton.setContentAreaFilled(false);

			udpButtonPanel.add(udpButton);

			JPanel udpTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
			udpTextPanel.setOpaque(false);

			udpLabel = new JLabel("UDP");
			udpLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
			udpLabel.setForeground(Color.WHITE);

			udpTextPanel.add(udpLabel);

			JPanel udpPanel = new JPanel();
			udpPanel.setLayout(new BoxLayout(udpPanel, BoxLayout.X_AXIS));
			udpPanel.setOpaque(false);

			udpPanel.add(udpButtonPanel);
			udpPanel.add(udpTextPanel);

			// -----------

			JPanel gravacaoButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
			gravacaoButtonPanel.setOpaque(false);

			gravacaoButton = new JButton();
			gravacaoButton.setIcon(ICON_GRAVACAO_OFF);
			gravacaoButton.setPreferredSize(new Dimension(22, 22));
			gravacaoButton.setBorder(BorderFactory.createEmptyBorder());
			gravacaoButton.setContentAreaFilled(false);

			gravacaoButtonPanel.add(gravacaoButton);

			JPanel gravacaoTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
			gravacaoTextPanel.setOpaque(false);

			// Cria o texto do menu
			gravacaoLabel = new JLabel("Gravação");
			gravacaoLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
			gravacaoLabel.setForeground(Color.WHITE);

			gravacaoTextPanel.add(gravacaoLabel);

			JPanel gravacaoPanel = new JPanel();
			gravacaoPanel.setLayout(new BoxLayout(gravacaoPanel, BoxLayout.X_AXIS));
			gravacaoPanel.setOpaque(false);

			gravacaoPanel.add(gravacaoButtonPanel);
			gravacaoPanel.add(gravacaoTextPanel);

			// ----------

			// Criação do separador
			separator2 = new JSeparator(SwingConstants.HORIZONTAL);
			separator2.setPreferredSize(new Dimension(120, 1)); // Largura do separador, ajustada ao tamanho do menu
			separator2.setBackground(Color.GRAY); // Cor do separador
			separator2.setOpaque(true); // Deixa o separador visível

			// ---- cenarios ---

			if(listaCenarios!=null && !listaCenarios.isEmpty()) {
				listaCenarios.forEach((k, v) -> logger.info("Cenário {} -> {}", k, v));
			}
			
			// Criando os botões com seus respectivos estados
			//JPanel bladePanel = createButtonPanel("BLADE", () -> isBladeFixed, value -> isBladeFixed = value);
			//JPanel aroundPanel = createButtonPanel("AROUND", () -> isAroundFixed, value -> isAroundFixed = value);
			//JPanel ripperPanel = createButtonPanel("RIPPER", () -> isRipperFixed, value -> isRipperFixed = value);

			//

			// Criação do separador
			separator3 = new JSeparator(SwingConstants.HORIZONTAL);
			separator3.setPreferredSize(new Dimension(120, 1)); // Largura do separador, ajustada ao tamanho do menu
			separator3.setBackground(Color.GRAY); // Cor do separador
			separator3.setOpaque(true); // Deixa o separador visível

			JPanel sairTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
			sairTextPanel.setOpaque(false);

			JLabel sairLabel = new JLabel("Sair");
			sairLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
			sairLabel.setForeground(Color.GRAY);
			sairLabel.setIconTextGap(9);
			sairLabel.setIcon(ICON_SAIR_HOVER);
			sairTextPanel.add(sairLabel);

			JPanel sairPanel = new JPanel();
			sairPanel.setLayout(new BoxLayout(sairPanel, BoxLayout.X_AXIS));
			sairPanel.setOpaque(false);
			sairPanel.add(sairTextPanel);

			sairPanel.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e) {
			        logger.info("Iniciando o processo de saída do sistema.");

			        new Thread(() -> {
			            try {

							SwingUtilities.invokeAndWait(() -> {
								limparGravacao();
							});
			            	
			                logger.info("Encerrando interface gráfica...");

			                desfazerInicializacoesGridFree();
			                liberarRecursosWebBrowser();

			                if (webViewExecutor != null && !webViewExecutor.isShutdown()) {
			                    webViewExecutor.shutdownNow();
			                    if (!webViewExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
			                        logger.warn("webViewExecutor não encerrou no tempo esperado.");
			                    }
			                }

			                if (vncExecutor != null && !vncExecutor.isShutdown()) {
			                	vncExecutor.shutdownNow();
			                    if (!vncExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
			                        logger.warn("vncExecutor não encerrou no tempo esperado.");
			                    }
			                }
			                
			                if (gravacaoExecutor != null && !gravacaoExecutor.isShutdown()) {
			                	gravacaoExecutor.shutdownNow();
			                    if (!gravacaoExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
			                        logger.warn("gravacaoExecutor não encerrou no tempo esperado.");
			                    }
			                }
			                
			                logger.info("Parando pipelines e comunicação...");
			                stopCommunicationUDP();

			                logger.info("Finalizando GStreamer...");
			                stopGStreamer();

			                logger.info("Aguardando antes do encerramento final...");
			                Thread.sleep(500);

			                logger.info("Sistema sendo finalizado. Encerrando a JVM.");
			                System.exit(0);

			            } catch (Exception ex) {
			                logger.error("Erro ao finalizar recursos: ", ex);
			            }
			        }, "Saida-Thread").start();
			        			        
			    }
			    
			    @Override
				public void mouseEntered(MouseEvent e) {
					sairLabel.setIcon(ICON_SAIR);
					sairLabel.setForeground(Color.WHITE);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					sairLabel.setIcon(ICON_SAIR_HOVER);
					sairLabel.setForeground(Color.GRAY);
				}
			    
			});

			// Criação do separador
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			separator.setPreferredSize(new Dimension(120, 1)); // Largura do separador, ajustada ao tamanho do menu
			separator.setBackground(Color.GRAY); // Cor do separador
			separator.setOpaque(true); // Deixa o separador visível

			// Painel para o botão PTZ
			JPanel ptzButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
			ptzButtonPanel.setOpaque(false);

			// Botão PTZ
			ptzButton = new JButton();
			ptzButton.setIcon(ICON_PTZ_OFF); // Ícone do botão PTZ
			ptzButton.setPreferredSize(new Dimension(22, 22));
			ptzButton.setBorder(BorderFactory.createEmptyBorder());
			ptzButton.setContentAreaFilled(false);

			// Adiciona o botão ao painel
			ptzButtonPanel.add(ptzButton);

			// Painel para o texto do botão PTZ
			JPanel ptzTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
			ptzTextPanel.setOpaque(false);

			JLabel ptzLabel = new JLabel("PTZ");
			ptzLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
			ptzLabel.setForeground(Color.GRAY);

			// Adiciona o texto ao painel
			ptzTextPanel.add(ptzLabel);

			// Painel final do PTZ (botão + texto)
			JPanel ptzPanel = new JPanel();
			ptzPanel.setLayout(new BoxLayout(ptzPanel, BoxLayout.X_AXIS));
			ptzPanel.setOpaque(false);
			ptzPanel.add(ptzButtonPanel);
			ptzPanel.add(ptzTextPanel);

			Map<String, Integer> cameraStates = new HashMap<>();

			ptzButton.addActionListener(e -> {
				if (isGridFreeShow) {
					// Cria o menu popup
					ptzMenu = new JPopupMenu();

					// Adiciona as opções PTZ dinamicamente com base na lista de câmeras
					ButtonGroup ptzGroup = new ButtonGroup();
					boolean hasPTZ = false; // Verifica se há câmeras com PTZ

					for (String codigo : mapaCameras.keySet()) {
						CameraDTO camera = mapaCameras.get(codigo);

						if (camera.getPTZ() > 0) {
							hasPTZ = true; // Indica que há pelo menos uma câmera com PTZ ativo

							// Nome da opção baseado no código da câmera
							JRadioButtonMenuItem ptzItem = new JRadioButtonMenuItem(codigo);

							if (isCameraActive(camera)) {
								ptzItem.setForeground(Color.GREEN);
								cameraStates.put(codigo, 1);
							} else {
								ptzItem.setForeground(Color.WHITE);
								cameraStates.put(codigo, 0);
							}

							ptzItem.addActionListener(event -> {
								logger.info("Verificando status da câmera: {}", camera.getCodigo());

							
									logger.info("Câmera {} inativa, iniciando tarefa...", camera.getCodigo());

									executor.submit(() -> {
										try {
											this.createPipeline(camera).run();
										} catch (Exception e1) {
											logger.error("Erro ao iniciar tarefa para câmera {}: {}",
													camera.getCodigo(), e1.getMessage(), e1);
										}
									});

									cameraStates.put(codigo, 1);
								

								processarConfiguracoesPTZ(cameraStates);
							});

							// Adiciona o item ao grupo e ao menu
							ptzGroup.add(ptzItem);
							ptzMenu.add(ptzItem);
						}
					}

					// Define o ícone do botão PTZ com base na presença de PTZ
					if (hasPTZ) {
						ptzButton.setIcon(ICON_PTZ_ON);
						ptzMenu.show(ptzButton, (ptzButton.getWidth() / 2) - 10, (ptzButton.getHeight() / 2) + 12);
						processarConfiguracoesPTZ(cameraStates);
					} else {
						ptzButton.setIcon(ICON_PTZ_OFF);
					}
				}
			});

			CenarioFileManager cenarioFileManager = new CenarioFileManager(usuario);

			sideMenuPanel.add(closeButtonPanel, 0);
			sideMenuPanel.add(gridFreePanel, 1);
			sideMenuPanel.add(memorizePanel, 2);
			sideMenuPanel.add(menuPanel, 3);
			sideMenuPanel.add(udpPanel, 4);
			sideMenuPanel.add(gravacaoPanel, 5);
			sideMenuPanel.add(separator2, 6);

			// adiciona cenários e descobre onde continuar
			Map<String, CenarioDTO> cenarios = cenarioFileManager.carregarCenarios();
			int qtd = renderScenarioButtons(cenarios);

			// calcula o próximo índice após os cenários
			int idxBase = indexOfComponent(sideMenuPanel, separator2) + 1 + qtd;

			// adiciona o restante a partir do índice calculado
			int idx = idxBase;
			
			// só adiciona separator3 se houver ao menos 1 cenário
			if (qtd > 0) {
			    sideMenuPanel.add(separator3, idx++);
			}
			
			sideMenuPanel.add(sairPanel,  idx++);
			sideMenuPanel.add(separator,  idx++);
			sideMenuPanel.add(ptzPanel,   idx++);
			
			menuWindow.setSize(130,390 + (qtd * 45));
			menuWindow.setVisible(true);

			udpDTO = UDPConfigurationManager.get();

			if (udpDTO != null && udpDTO.isAtivar()) {
				this.startCommunicationUDP();
			}

			frame.setVisible(true);

		} catch (Exception e) {
			logger.error("Erro durante a inicialização: ", e);
		}

	}

	static Rectangle getScreenBounds(Component c) {
	    Point p = new Point(0, 0);
	    SwingUtilities.convertPointToScreen(p, c);
	    return new Rectangle(p, c.getSize());
	}
	
	private int renderScenarioButtons(Map<String, CenarioDTO> cenarios) {
	    // remove o bloco antigo entre separator2 e separator3 (se existir)
	    int start = indexOfComponent(sideMenuPanel, separator2) + 1;
	    int end   = indexOfComponent(sideMenuPanel, separator3);
	    if (end == -1) end = sideMenuPanel.getComponentCount();
	    for (int i = end - 1; i >= start; i--) {
	        sideMenuPanel.remove(i);
	    }

	    // ordem alfabética (ajuste se quiser outra ordem)
	    java.util.List<String> nomes = new java.util.ArrayList<>(cenarios.keySet());
	    nomes.sort(String::compareToIgnoreCase);

	    // garante chaves no mapa de estado
	    for (String nome : nomes) {
	        scenarioFixed.putIfAbsent(nome.toLowerCase(), false);
	    }

	    // insere logo após o separator2
	    int insertIndex = start;
	    for (String nome : nomes) {
	        final String key = nome.toLowerCase();
	        JPanel panel = createButtonPanel(
	            nome.toUpperCase()
	        );
	        sideMenuPanel.add(panel, insertIndex++);
	    }

	    sideMenuPanel.revalidate();
	    sideMenuPanel.repaint();
	    return nomes.size(); // <- quantidade adicionada
	}

	private int indexOfComponent(Container parent, Component c) {
	    for (int i = 0; i < parent.getComponentCount(); i++) {
	        if (parent.getComponent(i) == c) return i;
	    }
	    return -1;
	}
	
	private void liberarRecursosWebBrowser() {
	    logger.info("Iniciando liberação dos recursos do Web Browser...");

	    if (mapaBrowser != null && !mapaBrowser.isEmpty()) {
	        CountDownLatch latch = new CountDownLatch(mapaBrowser.size());

	        mapaBrowser.forEach((webKey, browser) -> {
	            Display display = browser.getDisplay();

	            if (!browser.isDisposed() && !display.isDisposed()) {
	                display.asyncExec(() -> {
	                    try {
	                        browser.execute("try { for (let i = 1; i < 99999; i++) clearInterval(i); for (let i = 1; i < 99999; i++) clearTimeout(i); } catch(e) { console.log(e); }");
	                        browser.setUrl("about:blank");

	                        Shell shell = browser.getShell();
	                        browser.dispose();
	                        if (shell != null && !shell.isDisposed()) {
	                            shell.dispose();
	                        }
	                    } catch (Exception e) {
	                        logger.warn("Erro ao liberar browser '{}': {}", webKey, e.getMessage());
	                    } finally {
	                        latch.countDown();
	                    }
	                });
	            } else {
	                latch.countDown();
	            }

	            JInternalFrame frame = mapaInternalFrame.get(webKey);
	            if (frame != null) {
	                SwingUtilities.invokeLater(() -> {
	                    frame.dispose();
	                });
	                mapaInternalFrame.remove(webKey);
	            }
	        });

	        try {
	            latch.await(3, TimeUnit.SECONDS);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }

	        SwingUtilities.invokeLater(() -> {
	            mapaBrowser.clear();
	            mapaWebViewFrames.clear();
	        });
	    }

	    logger.info("Recursos do Web Browser liberados com sucesso.");
	}
	
	private void inicializaGridFree() {

		String scenarioPrefix = "";

		for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
		    String name  = e.getKey();
		    Boolean fixed = e.getValue();

		    if (Boolean.TRUE.equals(fixed)) {
		        scenarioPrefix = name.toLowerCase(Locale.ROOT);
		         break; 
		    }
		}

		CenarioFileManager manager = new CenarioFileManager(usuario);

		if (!scenarioPrefix.isEmpty()) {
		    if (manager.cenarioExiste(scenarioPrefix)) {
		        configuracaoVisual = loadVisualScenarioConfigurations(scenarioPrefix);
		    } else {
		        logger.warn("Cenário '{}' não encontrado. Resetando interface.", scenarioPrefix);
		        this.resetAllButtons();
		        this.toggleGridFree();
		        return;
		    }
		} else {
		    configuracaoVisual = loadConfigurationsFromVisual();
		}

		gridFreeLabel.setText("Aguarde...");
		gridFreeLabel.setFont(new Font("Roboto", Font.PLAIN, 12));

		if (configuracaoVisual != null && !configuracaoVisual.isEmpty()) {
			memorizeButton.setIcon(ICON_FIXAR_ON);
		}

		mapaConfiguracaoVisualCameras = mapearConfiguracaoVisualDasCamerasPorCodigo(configuracaoVisual);

		//configuracaoVisual = null;

		List<CameraDTO> cameras = CameraConfigurationManager.get();

		if (cameras == null || cameras.isEmpty()) {
			isGridFreeShow = false;
			exibirMensagemErro(
					"Nenhuma câmera foi encontrada. \nPor favor, verifique as configurações e tente novamente.",
					"Erro: Câmera Não Encontrada");
			return;
		}

		mapaCameras = mapearCamerasPorCodigo(cameras);

		// Aplica filtro de cenário, se existir
		if (!scenarioPrefix.isEmpty()) {
		    manager = new CenarioFileManager(usuario);
		    Map<String, CenarioDTO> cenarios = manager.carregarCenarios();
		    CenarioDTO cenario = cenarios.get(scenarioPrefix);

		    if (cenario != null && cenario.getCodigoCameras() != null) {
		        List<String> listaCodigoCameras = cenario.getCodigoCameras();

		        Set<String> anteriores = new HashSet<>(mapaCameras.keySet());
		        mapaCameras.keySet().retainAll(listaCodigoCameras);

		        anteriores.removeAll(mapaCameras.keySet());
		        logger.info("Câmeras removidas por filtro de cenário '{}': {}", scenarioPrefix, anteriores);
		    }
		}

		cameras = null;

		if (mapaCameras.isEmpty()) {
			isGridFreeShow = false;
			exibirMensagemErro(
					"Nenhuma câmera foi encontrada. \nPor favor, verifique as configurações e tente novamente.",
					"Erro: Câmera Não Encontrada");
			return;
		}

		performanceDTO = PerformanceConfigurationManager.get();
		if (performanceDTO == null) {
			isGridFreeShow = false;
			exibirMensagemErro(
					"A configuração de performance não foi encontrada. \nPor favor, verifique as configurações e tente novamente.",
					"Erro de Configuração");
			return;
		}

		// Prepara contadores
		for (String codigo : mapaCameras.keySet()) {
			mapaStartTime.put(codigo, -1L);
			mapaFrameCount.put(codigo, 0);
		}

		// Protege thread de animação
		if (animationThread != null && animationThread.isAlive()) {
			animationThread.interrupt();
		}
		animationThread = createAnimationThread();
		animationThread.start();

		// Reinicializa executor
		// shutdownExecutor(); // FINALIZA o anterior antes de criar novo

		this.shutdownExecutor();

		int camerasCount = mapaCameras.size();
		int extras = Math.min(4, camerasCount / 2);

		executor = new ThreadPoolExecutor(camerasCount, camerasCount + extras, 1L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy());

		// Enfileira tarefas para câmeras
		camerasEstadoZero = loadConfigurationsFromVisualPTZ().stream().filter(config -> config.getEstado() == 0)
				.collect(Collectors.toList());

		// 1. Limpa todas as câmeras antes de reinicializar
		mapaCameras.keySet().forEach(this::cleanupCameraResources);

		// 2. Reinicializa somente as que não estão em estado zero
		mapaCameras.forEach((codigo, camera) -> {
			boolean deveInicializar = camerasEstadoZero.stream()
					.noneMatch(config -> config.getCodigoPTZ().equals(codigo));

			if (deveInicializar) {

				executor.submit(this.createPipeline(camera));
			}
		});

		WebBrowserDTO webBrowserDTO = WebBrowserConfigurationManager.get();
		if (webBrowserDTO != null && webBrowserDTO.isAtivar()) {

		    // Permite threads simultâneas (ou pode usar newFixedThreadPool(2))
		    webViewExecutor = Executors.newCachedThreadPool();

		    // Para cada URL, dispara uma thread
		    if (webBrowserDTO.getUrl1() != null && !webBrowserDTO.getUrl1().isEmpty()) {
		        final String url1 = webBrowserDTO.getUrl1();
		        webViewExecutor.execute(() -> criarSwtInternalFrame("WEB-1", url1));
		    }
		    if (webBrowserDTO.getUrl2() != null && !webBrowserDTO.getUrl2().isEmpty()) {
		        final String url2 = webBrowserDTO.getUrl2();
		        webViewExecutor.execute(() -> criarSwtInternalFrame("WEB-2", url2));
		    }
		}
		
		
		VNCDTO vncDTO = VNCConfigurationManager.get();
		if (vncDTO != null && vncDTO.isAtivar()) {
			vncExecutor = Executors.newCachedThreadPool();
		    vncExecutor.execute(() -> initializeVNC());
		}

		GravacaoDTO gravacaoDTO = GravacaoConfigurationManager.get();
		if (gravacaoDTO != null && gravacaoDTO.isAtivar()) {

		    String caminho = gravacaoDTO.getCaminho();

		    // 1) Caminho válido (sem acentos/esp/esp)
		    if (!isPathValido(caminho)) {
		        logger.error("Caminho de gravação inválido: {}", caminho);
		        exibirMensagemErro(
		            "O caminho de gravação contém caracteres inválidos (acentos, cedilha, espaços ou símbolos especiais).\n" +
		            "Por favor, reconfigure o diretório de gravação em Configurações > Gravação.\n\n" +
		            "Caminho atual: " + caminho,
		            "Erro de Configuração"
		        );
		        return;
		    }

		    // 2) Espaço disponível (limite 10%)
		    if (!temEspacoSuficiente(caminho, 10.0)) {
		        // Detalhe para o usuário (total/livre/%)
		        String detalhe = obterDetalheEspaco(caminho);
		        logger.error("Espaço insuficiente para gravação no diretório: {} | {}", caminho, detalhe);
		        exibirMensagemErro(
		            "O drive de gravação possui menos de 10% de espaço livre.\n" +
		            "A gravação foi interrompida para evitar falhas.\n\n" +
		            detalhe + "\n\n" +
		            "Por favor, libere espaço ou selecione outro diretório de gravação.",
		            "Espaço Insuficiente"
		        );
		        return;
		    }

		    // 3) Tudo OK → inicia a gravação
		    gravacaoExecutor = Executors.newCachedThreadPool();
		    gravacaoExecutor.execute(() -> initializeGravacao());
		}
	}

	
	/**
	 * Verifica se o drive do caminho possui pelo menos 'minPercentLivres'% de espaço livre.
	 */
	private boolean temEspacoSuficiente(String caminho, double minPercentLivres) {
	    try {
	        File dir = new File(caminho);
	        
	        if (!dir.exists()) {
	            dir = dir.getParentFile();
	            if (dir == null || !dir.exists()) {
	                logger.warn("Diretório pai inexistente para: {}", caminho);
	                return false;
	            }
	        }
	        
	        if (dir == null || !dir.exists()) {
	            logger.warn("Diretório não encontrado para verificação de espaço: {}", caminho);
	            return false;
	        }

	        long total = dir.getTotalSpace();   // bytes
	        long livre = dir.getUsableSpace();  // bytes (para o usuário atual)

	        if (total <= 0 || livre <= 0) {
	            logger.warn("Não foi possível obter informações de espaço do volume para: {}", caminho);
	            return false;
	        }

	        double percentualLivre = (livre * 100.0) / total;

	        // Log informativo detalhado
	        logger.info("Espaço no drive de gravação ({}): total={}, livre={}, livre%={}",
	            caminho, humanBytes(total), humanBytes(livre), formatPercent(percentualLivre));

	        return percentualLivre >= minPercentLivres;
	    } catch (Exception e) {
	        logger.error("Erro ao verificar espaço livre em {}", caminho, e);
	        return false;
	    }
	}

	/**
	 * Retorna uma string detalhando total/livre/% para exibir ao usuário.
	 * Ex.: "Total: 931.51 GB, Livre: 42.37 GB (4.55%)"
	 */
	private String obterDetalheEspaco(String caminho) {
	    try {
	        File dir = new File(caminho);
	        if (!dir.exists()) {
	            dir = dir.getParentFile();
	        }
	        if (dir == null || !dir.exists()) {
	            return "Volume não encontrado para: " + caminho;
	        }

	        long total = dir.getTotalSpace();
	        long livre = dir.getUsableSpace();
	        if (total <= 0) {
	            return "Não foi possível obter informações do volume.";
	        }
	        double pct = (livre * 100.0) / total;

	        return "Total: " + humanBytes(total) + ", Livre: " + humanBytes(livre) + " (" + formatPercent(pct) + ")";
	    } catch (Exception e) {
	        return "Não foi possível obter informações do volume (" + e.getMessage() + ").";
	    }
	}

	/** Converte bytes para string em GB com 2 casas (ex.: 42.37 GB) */
	private String humanBytes(long bytes) {
	    double gb = bytes / (1024.0 * 1024.0 * 1024.0);
	    DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
	    return df.format(gb) + " GB";
	}

	/** Formata percentual com 2 casas (ex.: 4.55%) */
	private String formatPercent(double p) {
	    DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
	    return df.format(p) + "%";
	}
	
	/**
	 * Valida se o caminho contém apenas caracteres permitidos.
	 * Permitidos: letras, números, barra, traço, underscore, ponto e dois-pontos.
	 */
	private boolean isPathValido(String caminho) {
	    if (caminho == null || caminho.isEmpty()) {
	        return false;
	    }

	    return caminho.matches("^[A-Za-z0-9_\\-/\\.]+$");
	}
	
	private void criarSwtInternalFrame(String webKey, String url) {
	    // Tenta recuperar objetos existentes
	    Browser navegadorExistente = mapaBrowser.get(webKey);
	    JInternalFrame frameExistente = mapaInternalFrame.get(webKey);

	    boolean browserOk = navegadorExistente != null && !navegadorExistente.isDisposed();
	    boolean frameOk = frameExistente != null && frameExistente.isVisible();

	    if (browserOk && frameOk) {
	        Display display = navegadorExistente.getDisplay();
	        if (!display.isDisposed()) {
	            display.asyncExec(() -> navegadorExistente.setUrl(url));
	        }
	        frameExistente.setTitle(webKey + ": " + url);
	        frameExistente.setVisible(true);
	        frameExistente.toFront();
	        return;
	    } else {
	        // Remove entradas inválidas dos mapas, se necessário
	        if (!browserOk) mapaBrowser.remove(webKey);
	        if (!frameOk) mapaInternalFrame.remove(webKey);
	        mapaWebViewFrames.remove(webKey);
	    }

	    // Cria o JInternalFrame estilizado
	    JInternalFrame frame = new JInternalFrame(webKey, true, false, false, false) {
	        @Override
	        public void updateUI() {
	            super.updateUI();
	            BasicInternalFrameUI ui = (BasicInternalFrameUI) getUI();
	            if (ui.getNorthPane() != null) {
	                MouseInputAdapter mouseAdapter = criarMouseAdapterGenerico("WEB-" + webKey);
	                ui.getNorthPane().addMouseListener(mouseAdapter);
	                ui.getNorthPane().addMouseMotionListener(mouseAdapter);
	                for (Component comp : ui.getNorthPane().getComponents()) {
	                    if (comp instanceof JButton) comp.setVisible(false);
	                }
	                ui.getNorthPane().setPreferredSize(new Dimension(getWidth(), 24));
	            }
	        }
	    };
	    frame.setFrameIcon(new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE)));
	    frame.setTitle(webKey);
	    frame.setLayout(new BorderLayout());

	    // Painel Swing + Canvas para SWT
	    JPanel painelFundo = new JPanel(new BorderLayout());
	    painelFundo.setBackground(Color.BLACK);
	    Canvas canvas = new Canvas();
	    painelFundo.add(canvas, BorderLayout.CENTER);
	    frame.getContentPane().add(painelFundo, BorderLayout.CENTER);

	    layeredPane.add(frame, JLayeredPane.DEFAULT_LAYER);

	    // Aplica visual e posicionamento
	    aplicarConfiguracaoVisual(webKey, frame, url);

	    // Adiciona listener para mover/redimensionar o frame e atualizar o browser
	    frame.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentMoved(ComponentEvent e) {
	            Browser browser = mapaBrowser.get(webKey);
	            if (browser != null && !browser.isDisposed()) {
	                Display display = browser.getDisplay();
	                if (!display.isDisposed()) {
	                    display.asyncExec(() -> {
	                        if (!browser.isDisposed()) {
	                            Rectangle bounds = canvas.getBounds();
	                            browser.setBounds(0, 0, bounds.width, bounds.height);
	                        }
	                    });
	                }
	            }
	        }
	        @Override
	        public void componentResized(ComponentEvent e) {
	            Browser browser = mapaBrowser.get(webKey);
	            if (browser != null && !browser.isDisposed()) {
	                Display display = browser.getDisplay();
	                if (!display.isDisposed()) {
	                    display.asyncExec(() -> {
	                        if (!browser.isDisposed()) {
	                            Rectangle bounds = canvas.getBounds();
	                            browser.setBounds(0, 0, bounds.width, bounds.height);
	                        }
	                    });
	                }
	            }
	        }
	    });

	    // Torna frame visível
	    SwingUtilities.invokeLater(() -> {
	        frame.setVisible(true);
	        frame.revalidate();
	        frame.repaint();
	    });

	    // Inicializa o SWT Browser em thread separada
	    webViewExecutor.submit(() -> {
	        Display display = new Display();
	        Shell shell = SWT_AWT.new_Shell(display, canvas);
	        shell.setLayout(null);

	        Browser browser = new Browser(shell, SWT.NONE);
	        browser.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	        browser.setUrl(url);

	        shell.setSize(canvas.getWidth(), canvas.getHeight());
	        shell.open();

	        // Guarda os objetos nos mapas
	        mapaBrowser.put(webKey, browser);
	        mapaInternalFrame.put(webKey, frame);
	        mapaWebViewFrames.put(webKey, frame);

	        // Ajusta o navegador quando o canvas for redimensionado
	        canvas.addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {
	                if (!browser.isDisposed() && !display.isDisposed()) {
	                    display.asyncExec(() -> {
	                        if (!browser.isDisposed()) {
	                            Rectangle bounds = canvas.getBounds();
	                            browser.setBounds(0, 0, bounds.width, bounds.height);
	                            shell.setSize(bounds.width, bounds.height);
	                        }
	                    });
	                }
	            }
	        });

	        // Cleanup: ao fechar frame, remove navegador e recursos
	        frame.addInternalFrameListener(new InternalFrameAdapter() {
	            @Override
	            public void internalFrameClosing(InternalFrameEvent e) {
	                try {
	                    display.asyncExec(() -> {
	                        if (!browser.isDisposed()) {
	                            browser.setUrl("about:blank");
	                            browser.dispose();
	                        }
	                    });
	                    mapaBrowser.remove(webKey);
	                    mapaInternalFrame.remove(webKey);
	                    mapaWebViewFrames.remove(webKey);
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }
	        });

	        // Loop SWT
	        while (!shell.isDisposed()) {
	            if (!display.readAndDispatch()) {
	                display.sleep();
	            }
	        }
	        display.dispose();
	    });
	}


	private void stopGStreamer() {
		if (Gst.isInitialized()) {
			this.cleanupPipelines();
			Gst.quit(); // Fecha GStreamer corretamente
			logger.info("GStreamer finalizado."); // Somente GStreamer foi finalizado aqui
		}
	}

	public void desfazerInicializacoesGridFree() {
	    logger.info("Desfazendo inicializações do Grid Free...");

	    selectedIndex = -1;
	    
	    scenarioFixed.replaceAll((k, v) -> false);
	    
	    // Interrompe thread de animação
	    if (animationThread != null && animationThread.isAlive()) {
	        animationThread.interrupt();
	        animationThread = null;
	        logger.info("Thread de animação interrompida.");
	    }

	    // Encerra Executor de câmeras
	    if (executor != null && !executor.isShutdown()) {
	        executor.shutdownNow();
	        try {
	            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
	                logger.warn("Executor não encerrou no tempo esperado.");
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	        executor = null;
	        logger.info("Executor de câmeras encerrado.");
	    }
	   

	 // Liberação correta dos recursos SWT evitando "Invalid thread access"
	    for (String webKey : new String[]{"WEB-1", "WEB-2"}) {
	        Browser browser = mapaBrowser.remove(webKey);

	        if (browser != null && !browser.isDisposed()) {
	            Display display = browser.getDisplay();

	            if (!display.isDisposed()) {
	                display.asyncExec(() -> {
	                    if (!browser.isDisposed()) {
	                        Shell shell = browser.getShell();  // Agora corretamente dentro do asyncExec
	                        browser.setUrl("about:blank");
	                        browser.dispose();

	                        if (shell != null && !shell.isDisposed()) {
	                            shell.dispose();
	                        }

	                        if (!display.isDisposed()) {
	                            display.dispose();
	                        }
	                    }
	                });
	            }
	        }

	        JInternalFrame frame = mapaInternalFrame.remove(webKey);
	        if (frame != null && frame.isDisplayable()) {
	            SwingUtilities.invokeLater(frame::dispose);
	        }

	        mapaWebViewFrames.remove(webKey);
	    }

	    mapaBrowser.clear();

	    // Agora sim encerra o executor do WebView com segurança após finalizar SWT
	    if (webViewExecutor != null && !webViewExecutor.isShutdown()) {
	        webViewExecutor.shutdownNow();
	        try {
	            if (!webViewExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
	                logger.warn("WebViewExecutor não encerrou no tempo esperado.");
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	        webViewExecutor = null;
	        logger.info("WebViewExecutor encerrado.");
	    }

	    // Agora sim encerra o executor do vncExecutor com segurança após finalizar SWT
	    if (vncExecutor != null && !vncExecutor.isShutdown()) {
	    	vncExecutor.shutdownNow();
	        try {
	            if (!vncExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
	                logger.warn("vncExecutor não encerrou no tempo esperado.");
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	        vncExecutor = null;
	        logger.info("vncExecutor encerrado.");
	    }
	    
	    // Agora sim encerra o executor do gravacaoExecutor com segurança após finalizar SWT
	    if (gravacaoExecutor != null && !gravacaoExecutor.isShutdown()) {
	    	gravacaoExecutor.shutdownNow();
	        try {
	            if (!gravacaoExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
	                logger.warn("gravacaoExecutor não encerrou no tempo esperado.");
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	        gravacaoExecutor = null;
	        logger.info("gravacaoExecutor encerrado.");
	    }
	    
	    // Destrói pipelines das câmeras explicitamente
	    if (mapaPipeline != null) {
	        mapaPipeline.keySet().forEach(this::destroyPipeline);
	        mapaPipeline.clear();
	        logger.info("Todos os pipelines destruídos.");
	    }

	    // Limpa handlers do bus
	    if (mapaBusErrorHandlers != null) mapaBusErrorHandlers.clear();
	    if (mapaBusStateHandlers != null) mapaBusStateHandlers.clear();
	    if (mapaBusReconnectHandlers != null) mapaBusReconnectHandlers.clear();

	    // Limpa mapas e listas
	    if (mapaCameras != null) mapaCameras.clear();
	    if (mapaConfiguracaoVisualCameras != null) mapaConfiguracaoVisualCameras.clear();
	    if (mapaStartTime != null) mapaStartTime.clear();
	    if (mapaFrameCount != null) mapaFrameCount.clear();
	    if (camerasEstadoZero != null) camerasEstadoZero.clear();

	    // Descarta frames internos restantes (geral)
	    if (mapaInternalFrame != null) {
	        for (JInternalFrame frame : mapaInternalFrame.values()) {
	            if (frame != null && frame.isDisplayable()) {
	                try {
	                    if (SwingUtilities.isEventDispatchThread()) {
	                        frame.dispose();
	                    } else {
	                        SwingUtilities.invokeAndWait(frame::dispose);
	                    }
	                } catch (Exception e) {
	                    logger.warn("Erro ao descartar JInternalFrame", e);
	                }
	            }
	        }
	        mapaInternalFrame.clear();
	        logger.info("InternalFrames descartados.");
	    }

	    // Reset visual UI
	    SwingUtilities.invokeLater(() -> {
	        gridFreeLabel.setText("");
	        memorizeButton.setIcon(ICON_FIXAR_OFF);
	        resetAllButtons();
	    });

	    logger.info("Interface resetada e frames fechados.");
	}
	
	private void destroyPipeline(String codigo) {
	    Pipeline oldPipeline = mapaPipeline.remove(codigo);
	    if (oldPipeline != null) {
	        try {
	            // Primeiro coloca o pipeline em estado NULL
	            oldPipeline.setState(State.NULL);
	            State state = oldPipeline.getState(5, TimeUnit.SECONDS);
	            
	            // Se após 5 segundos não ficou em NULL, gera um alerta (opcional)
	            if (state != State.NULL) {
	                logger.warn("Pipeline {} não atingiu estado NULL após 5s. Estado atual: {}", codigo, state);
	            }

	            // Libera os recursos do pipeline explicitamente
	            oldPipeline.dispose();
	            
	            Pipeline newPipeline = new Pipeline();
                mapaPipeline.put(codigo, newPipeline);
	            
	            // Remove do mapa todos os recursos associados
	            mapaPad.remove(codigo);
	            mapaFps.remove(codigo);
	            mapaFrameCount.remove(codigo);
	            mapaStartTime.remove(codigo);
	            mapaLastPacketTime.remove(codigo);
	            mapaLatenciaPacote.remove(codigo);
	            mapaLatenciasPacote.remove(codigo);

	            logger.info("Pipeline destruído com sucesso para {}", codigo);
	        } catch (Exception e) {
	            logger.error("Erro ao destruir pipeline {}", codigo, e);
	        }
	    }
	}
	
	public void toggleGridFree() {
	    heartbeatCounter = 0;

	    if (isGridFreeShow) {
	        desativarModoGridFree();
	    } else {
	        // Limpeza rápida e eficiente antes de ativar
	        liberarBrowsersSWT();
	        limparFramesSwing();
	        ativarModoGridFree();
	    }
	}

	private void liberarBrowsersSWT() {
	    logger.info("Liberando navegadores SWT...");

	    for (String webKey : new String[]{"WEB-1", "WEB-2"}) {
	        Browser browser = mapaBrowser.remove(webKey);

	        if (browser != null && !browser.isDisposed()) {
	            Display display = browser.getDisplay();

	            if (!display.isDisposed()) {
	                display.asyncExec(() -> {
	                    if (!browser.isDisposed()) {
	                        Shell shell = browser.getShell();
	                        browser.setUrl("about:blank");
	                        browser.dispose();

	                        if (shell != null && !shell.isDisposed()) {
	                            shell.dispose();
	                        }
	                    }
	                    if (!display.isDisposed()) {
	                        display.dispose();
	                    }
	                });
	            }
	        }

	        JInternalFrame frame = mapaInternalFrame.remove(webKey);
	        if (frame != null && frame.isDisplayable()) {
	            SwingUtilities.invokeLater(frame::dispose);
	        }

	        mapaWebViewFrames.remove(webKey);
	    }

	    mapaBrowser.clear();
	}
	
	private void limparFramesSwing() {
	    if (mapaInternalFrame != null) {
	        mapaInternalFrame.values().forEach(frame -> {
	            if (frame != null && frame.isDisplayable()) {
	                SwingUtilities.invokeLater(frame::dispose);
	            }
	        });
	        mapaInternalFrame.clear();
	    }
	}
	
	private void desativarModoGridFree() {
	    logger.info("Desativando o modo Grid Free...");
	    isGridFreeShow = false;
	    desfazerInicializacoesGridFree();
	}
	
	private void ativarModoGridFree() {
	    logger.info("Ativando o modo Grid Free...");
	    contadorThreadsIniciadas = 0;
	    isGridFreeShow = true;
	    listaParaStatusCamera = null;
	    inicializaGridFree();
	}

	private void diagnosticarThreadsEMemoria(String momento) {
		System.gc();
		try {
			Thread.sleep(2000); // Tempo para GC agir
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		long usedMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
		System.out.println("Memória usada " + momento + ": " + usedMB + " MB");

		System.out.println("Threads vivas " + momento + ":");
		Thread.getAllStackTraces().keySet().stream().map(t -> t.getName() + " [" + t.getState() + "]").sorted()
				.forEach(System.out::println);
	}

	private void resetAllButtons() {
		SwingUtilities.invokeLater(() -> {
			// Resetando Grid e botões auxiliares
			gridFreeLabel.setIcon(ICON_GRID_FREE_HOVER);
			gridFreeLabel.setText("Abrir Grid");
			gridFreeLabel.setForeground(Color.GRAY);

			memorizeButton.setIcon(ICON_FIXAR_OFF);
			ptzButton.setIcon(ICON_PTZ_OFF);

			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).setIcon(ICON_CENARIO_OFF);
				labels.get(i).setForeground(Color.GRAY);
			}

			// Resetando os estados booleanos
			isBladeFixed = false;
			isAroundFixed = false;
			isRipperFixed = false;

			isGridFreeShow = false;

		});
	}

	/*
	 * private void limparGravacao() { synchronized (this) { // Garante segurança na
	 * manipulação de screenRecorder if (screenRecorder != null &&
	 * screenRecorder.isRecording()) {
	 * logger.info("Finalizando gravação de tela...");
	 * 
	 * try { screenRecorder.stopAllRecords(); // Para todas as gravações
	 * screenRecorder = null; // Remove a referência após a finalização
	 * 
	 * SwingUtilities.invokeLater(() -> { gravacaoButton.setIcon(ICON_GRAVACAO_OFF);
	 * // Atualiza a UI corretamente });
	 * 
	 * logger.info("Gravação de tela finalizada e memória liberada."); } catch
	 * (Exception e) { logger.error("Erro ao interromper a gravação de tela", e); }
	 * } } }
	 */

	private void shutdownExecutor() {
		
		if (executor != null && !executor.isShutdown()) {
			logger.info("Encerrando executor anterior...");
			executor.shutdown();
			try {
				if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
					logger.warn("Executor não finalizou em 3s, forçando encerramento.");
					executor.shutdownNow();
				}				
				
				 if (webViewExecutor != null && !webViewExecutor.isShutdown()) {
		             webViewExecutor.shutdownNow();
		             if (!webViewExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
		                 logger.warn("webViewExecutor não encerrou no tempo esperado.");
		             }
		         }
		         
		         if (vncExecutor != null && !vncExecutor.isShutdown()) {
		         	vncExecutor.shutdownNow();
		             if (!vncExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
		                 logger.warn("vncExecutor não encerrou no tempo esperado.");
		             }
		         }
		         
		         if (gravacaoExecutor != null && !gravacaoExecutor.isShutdown()) {
		         	gravacaoExecutor.shutdownNow();
		             if (!gravacaoExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
		                 logger.warn("gravacaoExecutor não encerrou no tempo esperado.");
		             }
		         }
				
			} catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			} finally {
				executor = null;
				logger.info("Executor encerrado com sucesso.");
			}
		}
		
		
	}

	private synchronized void incrementarContadorEVerificar() {
		contadorThreadsIniciadas = contadorThreadsIniciadas + 1;

		if (contadorThreadsIniciadas == mapaCameras.size() - camerasEstadoZero.size()) {
			atualizarTextoBotaoGrid();
		}

	}

	private Thread createAnimationThread() {
		return new Thread(() -> {
			boolean isWhite = true;
			while (!Thread.currentThread().isInterrupted()) {
				final Color color = isWhite ? Color.WHITE : Color.GRAY;
				SwingUtilities.invokeLater(() -> {
					gridFreeLabel.setForeground(color);
				});
				isWhite = !isWhite;
				try {
					Thread.sleep(500); // Intervalo de piscar
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			gridFreeLabel.setForeground(Color.WHITE);
		});
	}

	private void atualizarTextoBotaoGrid() {

		Timer timer = new Timer(1000, null); // Timer para checar a cada 500 ms

		timer.addActionListener(e -> {

			// Obtém o número total de câmeras no mapa
			int totalCameras = mapaCameras.size();
			int camerasEstadoZeroCount = camerasEstadoZero.size();

			logger.info("Total de câmeras no mapa: {}", totalCameras);
			logger.info("Câmeras em estado 0: {}", camerasEstadoZeroCount);
			logger.info("Threads iniciadas: {}", contadorThreadsIniciadas);

			// Verifica se todas as câmeras estão prontas
			if (contadorThreadsIniciadas == (totalCameras - camerasEstadoZeroCount)) {
				// Interrompe o timer, pois todas as câmeras estão prontas
				timer.stop();

				// Atualiza o ícone e o texto do gridFreeLabel
				SwingUtilities.invokeLater(() -> {
					gridFreeLabel.setText("Fechar Grid");
					gridFreeLabel.setIcon(ICON_GRID_FREE_GREEN);

					for (CameraDTO camera : mapaCameras.values()) {
						if (camera.getPTZ() > 0) {
							ptzButton.setIcon(ICON_PTZ_ON);
						}
					}
				});

				if (animationThread != null && animationThread.isAlive()) {
					animationThread.interrupt(); // Para o piscar
				}

			}
		});

		timer.start(); // Inicia o timer
	}

	public static Map<String, CameraDTO> mapearCamerasPorCodigo(List<CameraDTO> cameras) {
		return cameras.stream().collect(Collectors.toMap(CameraDTO::getCodigo, camera -> camera));
	}

	public static Map<String, ConfiguracaoVisualDTO> mapearConfiguracaoVisualDasCamerasPorCodigo(
			List<ConfiguracaoVisualDTO> configuracoes) {
		return configuracoes.stream()
				.collect(Collectors.toMap(ConfiguracaoVisualDTO::getCodigoCamera, configuracao -> configuracao));
	}

	private MouseInputAdapter criarMouseAdapterGenerico(String logKey) {
		return new MouseInputAdapter() {
			private Point initialClick;
			private boolean dragging = false;

			@Override
			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				dragging = true;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragging && e.getComponent() instanceof JInternalFrame) {
					JInternalFrame frame = (JInternalFrame) e.getComponent();
					Point thisScreen = frame.getLocationOnScreen();
					Point mouseScreen = e.getLocationOnScreen();
					int xMoved = mouseScreen.x - initialClick.x - thisScreen.x;
					int yMoved = mouseScreen.y - initialClick.y - thisScreen.y;
					frame.setLocation(frame.getX() + xMoved, frame.getY() + yMoved);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dragging = false;
				SwingUtilities.invokeLater(() -> {
					if (memorizeButton != null) {
						memorizeButton.setIcon(ICON_FIXAR_OFF);
						logger.info("[{}] memorizeButton set to ICON_FIXAR_OFF", logKey);
					}
				});
			}
		};
	}

	private void aplicarConfiguracaoVisual(String webKey, JInternalFrame frame, String url) {

		ConfiguracaoVisualDTO configuracaoVisualDTO = mapaConfiguracaoVisualCameras.get(webKey);

		if (configuracaoVisualDTO == null) {
			configuracaoVisualDTO = mapaConfiguracaoVisualCameras.values().stream()
					.filter(config -> (webKey + ": " + url).contains(config.getCodigoCamera())).findFirst()
					.orElse(null);
		}

		if (configuracaoVisualDTO != null) {
			if (MonitorInfo.isValid(configuracaoVisualDTO.getPosicaoHorizontal(),
					configuracaoVisualDTO.getPosicaoVertical())) {
				frame.setLocation(configuracaoVisualDTO.getPosicaoHorizontal(),
						configuracaoVisualDTO.getPosicaoVertical());
			} else {
				frame.setLocation(0, 0);
			}
			frame.setSize(configuracaoVisualDTO.getTamanhoHorizontal(), configuracaoVisualDTO.getTamanhoVertical());
		} else {
			frame.setSize(1100, 146);
			frame.setLocation(0, 0);
		}

	}

	/*
	 * private MouseInputAdapter criarMouseAdapter(String webKey) { return new
	 * MouseInputAdapter() { private Point lastLocation;
	 * 
	 * @Override public void mousePressed(MouseEvent e) { lastLocation =
	 * e.getLocationOnScreen(); JInternalFrame frame =
	 * mapaInternalFrame.get(webKey); if (frame != null) { frame.toFront(); //
	 * Garante que o frame fique ativo frame.requestFocus(); // Força o foco }
	 * 
	 * }
	 * 
	 * @Override public void mouseReleased(MouseEvent e) {
	 * SwingUtilities.invokeLater(() -> {
	 * 
	 * if (memorizeButton != null) { memorizeButton.setIcon(ICON_FIXAR_OFF); }
	 * 
	 * }); }
	 * 
	 * 
	 * @Override public void mouseDragged(MouseEvent e) { JInternalFrame frame =
	 * mapaInternalFrame.get(webKey); if (frame != null && lastLocation != null) {
	 * Point newLocation = e.getLocationOnScreen(); int xDiff = newLocation.x -
	 * lastLocation.x; int yDiff = newLocation.y - lastLocation.y;
	 * 
	 * frame.setLocation(frame.getX() + xDiff, frame.getY() + yDiff); lastLocation =
	 * newLocation; } } }; }
	 */

	private void exibirMensagemErro(String mensagem, String titulo) {
		gridFreeLabel.setText("Abrir Grid");
		gridFreeLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
		gridFreeLabel.setForeground(Color.GRAY);
		gridFreeLabel.setIconTextGap(10);
		gridFreeLabel.setIcon(ICON_GRID_FREE_HOVER);

		JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
	}

	private void logMemoryAndMapStates(String contexto) {

		System.gc(); // Força coleta de lixo
		System.runFinalization();

		try {
			Thread.sleep(500); // Pequena pausa para GC agir
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		Runtime runtime = Runtime.getRuntime();
		double totalMemoryMB = runtime.totalMemory() / (1024.0 * 1024.0);
		double freeMemoryMB = runtime.freeMemory() / (1024.0 * 1024.0);
		double usedMemoryMB = totalMemoryMB - freeMemoryMB;
		double maxMemoryMB = runtime.maxMemory() / (1024.0 * 1024.0);

		logger.info("==== [{}] Uso de Memória ====", contexto);
		logger.info(String.format("Usado: %.2f MB | Livre: %.2f MB | Total Alocado: %.2f MB | Máximo JVM: %.2f MB",
				usedMemoryMB, freeMemoryMB, totalMemoryMB, maxMemoryMB));

		logger.info("==== [{}] Tamanhos dos Mapas ====", contexto);
		logger.info("mapaPipeline: {}", mapaPipeline.size());
		logger.info("mapaVideoComponente: {}", mapaVideoComponente.size());
		logger.info("mapaInternalFrame: {}", mapaInternalFrame.size());
		logger.info("mapaRotationOverlayPanel: {}", mapaRotationOverlayPanel.size());
		logger.info("mapaPTZOverlayPanel: {}", mapaPTZOverlayPanel.size());
		logger.info("mapaPad: {}", mapaPad.size());
		// logger.info("mapaMouseAdapter: {}", mapaMouseAdapter.size());
		logger.info("mapaWebViewFrames: {}", mapaWebViewFrames.size());
		logger.info("mapaVNCFrames: {}", mapaVNCFrames.size());
		logger.info("mapaOnvifDevice: {}", mapaOnvifDevice.size());
	}

	public static String getMemoryUsageString() {
		Runtime runtime = Runtime.getRuntime();

		double totalMemoryMB = runtime.totalMemory() / (1024.0 * 1024.0);
		double freeMemoryMB = runtime.freeMemory() / (1024.0 * 1024.0);
		double usedMemoryMB = totalMemoryMB - freeMemoryMB;
		double maxMemoryMB = runtime.maxMemory() / (1024.0 * 1024.0);

		return String.format("Usado: %.2f MB | Livre: %.2f MB | Total Alocado: %.2f MB | Máximo JVM: %.2f MB",
				usedMemoryMB, freeMemoryMB, totalMemoryMB, maxMemoryMB);
	}

	private void initializeGravacao() {
		synchronized (gravacaoLock) {
			if (gravacaoEmCriacao) {
				logger.warn("Gravação já está em processo de criação. Ignorando chamada duplicada.");
				return;
			}
			gravacaoEmCriacao = true;

			try {
				limparGravacao(); // sempre limpa antes

				GravacaoDTO gravacaoDTO = GravacaoConfigurationManager.get();
				if (gravacaoDTO == null || !gravacaoDTO.isAtivar()) {
					logger.info("Gravação desativada por configuração.");
					return;
				}

				if (screenRecorder == null) {
					screenRecorder = new ScreenRecorder();
				}

				if (!screenRecorder.isRecording()) {
					SwingUtilities.invokeLater(() -> gravacaoButton.setIcon(ICON_GRAVACAO_ON_GREEN));

					// Executa start em thread separada, se for bloqueante
					new Thread(() -> {
						try {
							screenRecorder.startCameras(pathvisualconf, usuario);
							logger.info("Gravação iniciada.");
						} catch (Exception e) {
							logger.error("Erro ao iniciar gravação", e);
							SwingUtilities.invokeLater(() -> gravacaoButton.setIcon(ICON_GRAVACAO_OFF));
						}
					}, "Gravacao-Thread").start();

				} else {
					logger.warn("Já existe uma gravação em andamento.");
				}

			} finally {
				gravacaoEmCriacao = false;
			}
		}
	}

	private void limparGravacao() {
		logger.info("Limpando recursos de gravação...");

		synchronized (gravacaoLock) {
			if (screenRecorder != null && screenRecorder.isRecording()) {
				try {
					screenRecorder.stopAllRecords(); // ✅ finaliza threads e buffers
					logger.info("Gravação encerrada com sucesso.");
				} catch (Exception e) {
					logger.error("Erro ao parar gravação", e);
				}
			}
			screenRecorder = null;

			SwingUtilities.invokeLater(() -> {
				gravacaoButton.setIcon(ICON_GRAVACAO_OFF);
			});

			logger.info("Gravador limpo e referências removidas.");
		}
	}

	private void initializeVNC() {
	    VNCDTO vncDTO = VNCConfigurationManager.get();
	    if (vncDTO == null || !vncDTO.isAtivar()) return;

	    SwingUtilities.invokeLater(() -> {
	        try {
	            String vncKey = "VNC";

	            JInternalFrame vncFrame = new JInternalFrame(null, true, false, false, false);
	            vncFrame.getContentPane().setLayout(new BorderLayout());

	            VNCPanel vncPanel = new VNCPanel();
	            vncPanel.setScaleToFit(false); // nítido (1:1). Use Ctrl+0/1/+/-

	            JScrollPane scroll = new JScrollPane(vncPanel);
	            scroll.getViewport().setBackground(java.awt.Color.BLACK);
	            scroll.setBorder(null);
	            vncFrame.getContentPane().add(scroll, BorderLayout.CENTER);

	            // sem ícone:
	            vncFrame.setFrameIcon(null);
		        
	            vncFrame.setTitle(vncKey);

	            // Configura tamanho e posição
	            configureVNCLocation(vncFrame, vncKey);

	            // Evita vazamento de listeners
	            MouseInputAdapter mouseAdapter = criarMouseAdapterGenerico("VNC" + vncKey);
	            vncFrame.addPropertyChangeListener("UI", evt -> {
	                BasicInternalFrameUI ui = (BasicInternalFrameUI) vncFrame.getUI();
	                if (ui.getNorthPane() != null) {
	                    JComponent north = ui.getNorthPane();
	                    for (var ml : north.getMouseListeners()) north.removeMouseListener(ml);
	                    for (var mml : north.getMouseMotionListeners()) north.removeMouseMotionListener(mml);
	                    north.addMouseListener(mouseAdapter);
	                    north.addMouseMotionListener(mouseAdapter);
	                    north.setPreferredSize(new Dimension(0, 20));
	                }
	            });

	            // Ordem correta
	            layeredPane.add(vncFrame, JLayeredPane.DEFAULT_LAYER);
	            vncFrame.setVisible(true);

	            // Mapas
	            mapaVNCFrames.put(vncKey, vncFrame);
	            mapaInternalFrame.put(vncKey, vncFrame);

	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null,
	                "Falha ao inicializar o painel VNC: " + e.getMessage(),
	                "Erro de Inicialização", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	}


	private void configureVNCLocation(JInternalFrame frame, String cameraCode) {
		
		ConfiguracaoVisualDTO configuracaoVisualDTO = configuracaoVisual.stream()
				.filter(dto -> cameraCode.contains(dto.getCodigoCamera())).findFirst().orElse(null);

		if (configuracaoVisualDTO != null) {
			if (MonitorInfo.isValid(configuracaoVisualDTO.getPosicaoHorizontal(),
					configuracaoVisualDTO.getPosicaoVertical())) {
				frame.setLocation(configuracaoVisualDTO.getPosicaoHorizontal(),
						configuracaoVisualDTO.getPosicaoVertical());
			} else {
				frame.setLocation(0, 0);
			}
			frame.setSize(configuracaoVisualDTO.getTamanhoHorizontal(), configuracaoVisualDTO.getTamanhoVertical());
		} else {
			frame.setSize(480, 270);
			frame.setLocation(0, 0);
		}
	}

	private void removerRecursosAntigos(String codigoCamera) {
	    // 1. Parar e remover pipeline antigo
	    Pipeline pipeline = mapaPipeline.remove(codigoCamera);
	    if (pipeline != null) {
	        try {
	            pipeline.setState(State.NULL);
	            pipeline.dispose();
	        } catch (Exception e) {
	            logger.warn("Erro ao parar e remover pipeline da câmera: " + codigoCamera, e);
	        }
	    }

	    GstVideoComponent videoComponent = mapaVideoComponente.remove(codigoCamera);
	    if (videoComponent != null) {
	        try {
	            Element element = videoComponent.getElement();
	            if (element != null) {
	                element.setState(State.NULL);
	                element.dispose(); // Esse é o elemento GStreamer interno
	            }
	        } catch (Exception e) {
	            logger.warn("Erro ao limpar elemento do GstVideoComponent da câmera: " + codigoCamera, e);
	        }
	    }

	    // 3. Remover Pad
	    mapaPad.remove(codigoCamera);

	    // 4. Remover métricas
	    mapaStartTime.remove(codigoCamera);
	    mapaFrameCount.remove(codigoCamera);
	    mapaLastPacketTime.remove(codigoCamera);
	    mapaLatenciasPacote.remove(codigoCamera);
	    mapaFps.remove(codigoCamera);
	    mapaLatenciaPacote.remove(codigoCamera);

	    // 5. Remover overlay PTZ e painel de rotação
	    mapaPTZOverlayPanel.remove(codigoCamera);
	    mapaRotationOverlayPanel.remove(codigoCamera);
	    mapaOnvifDevice.remove(codigoCamera);

	    // 6. Remover JInternalFrame
	    JInternalFrame frame = mapaInternalFrame.remove(codigoCamera);
	    if (frame != null) {
	        try {
	            frame.setVisible(false);
	            frame.dispose();
	        } catch (Exception e) {
	            logger.warn("Erro ao remover JInternalFrame da câmera: " + codigoCamera, e);
	        }
	    }

	    // 7. Última posição do mouse (drag)
	    mapaLastLocation.remove(codigoCamera);

	    // 8. Pipeline base (string do GStreamer)
	    mapaBasePipeline.remove(codigoCamera);
	    
	    logger.info("Todos os recursos da câmera " + codigoCamera + " foram removidos.");
	}
	
	private Runnable createPipeline(CameraDTO camera) {

		return (() -> {

			logger.info("Thread iniciada para câmera: " + camera.getCodigo());

			removerRecursosAntigos(camera.getCodigo());
			
			incrementarContadorEVerificar();

			// Prepara as informações da câmera (IP, porta, usuário, senha) para o pipeline
			// RTSP
			RSTPInfo rstpInfo = new RSTPInfo();
			rstpInfo.setIp(camera.getIp());
			rstpInfo.setPorta(camera.getPorta());
			rstpInfo.setExtensao(camera.getExtensao());
			rstpInfo.setUsuario(camera.getUsuario());
			rstpInfo.setSenha(camera.getSenha());

			String url = rstpInfo.montarURL();
			rstpInfo = null;

			// Determina o codec de vídeo baseado nas configurações da câmera
			String videoCodec = camera.getVideo().equals("H.264") ? "h264" : "h265";

			// Define o pipeline de áudio dependendo do tipo de áudio da câmera
			String audioPipeline = "";
			switch (camera.getAudio()) {
			case "PCM":
				audioPipeline = "rtpL16depay ! decodebin ! audioconvert ! audioresample ! autoaudiosink";
				break;
			case "AAC":
				audioPipeline = "rtpmp4gdepay ! aacparse ! mfaacdec ! audioconvert ! audioresample ! autoaudiosink";
				break;
			case "G.726":
				audioPipeline = "rtpg726depay ! avdec_g726 ! audioconvert ! audioresample ! autoaudiosink";
				break;
			default:
				audioPipeline = ""; // Se o áudio está desligado ou não suportado
			}

			String rotate = null;
			String flip = null;

			// Obtém a configuração visual da câmera
			ConfiguracaoVisualDTO configuracao = mapaConfiguracaoVisualCameras.get(camera.getCodigo());

			if (configuracao != null) {
				rotate = configuracao.getRotate();
				flip = configuracao.getFlip();
			} else {
				rotate = "0";
				flip = "none";
			}

			int largura = 0;
			int altura = 0;

			if (configuracao != null) {
				largura = configuracao.getTamanhoHorizontal();
				altura = configuracao.getTamanhoVertical();
			} else {
				largura = 480;
				altura = 270;
			}

			long bufferTimeNs = performanceDTO.getBufferSize() * 1_000_000L;
			String queue = "queue leaky=downstream max-size-time=" + bufferTimeNs
					+ " max-size-bytes=0 max-size-buffers=0 ! ";

			// Regex para detectar câmera DVR (ex: /cam3/mainstream)
			boolean isDvrLikeStream = url.matches(".*/cam\\d+/mainstream.*");

			String decoder = null;

			if (camera.getPTZ() == 1) {
				// Sempre usar hardware decoder para PTZ
				decoder = "avdec_" + videoCodec + " ! ";
			} else if (isDvrLikeStream) {
				// DVR-like streams sempre usam software decoder
				decoder = "avdec_" + videoCodec + " ! ";
			} else {
				// Caso normal segue configuração de aceleração
				if ("hardware".equalsIgnoreCase(performanceDTO.getAceleracao())) {
					decoder = "d3d11" + videoCodec + "dec ! ";
				} else {
					decoder = "avdec_" + videoCodec + " ! ";
				}
			}

			// Gera pipeline
			String basePipeline = "rtspsrc location=rtsp://" + url + " protocols=" + performanceDTO.getProtocolo()
					+ " latency=" + performanceDTO.getLatency() + " name=" + camera.getCodigo() + "source " +

					camera.getCodigo() + "source. ! "
					+ ("udp".equals(performanceDTO.getProtocolo())
							? "rtpjitterbuffer latency=" + performanceDTO.getLatency() + " ! "
							: "")
					+ queue + // Após rtspsrc

					"rtp" + videoCodec + "depay ! " + videoCodec + "parse config-interval=1 ! " + // 🔧 SPS/PPS regular
																									// para estabilidade
					queue + // Após parse

					decoder + // Fallback automático

					"identity silent=true ! watchdog timeout=" + (performanceDTO.getTimeout()+1500) + " ! " +

					queue + // Antes de pós-processamento
					"videoflip video-direction=" + rotate + " ! " + "videoflip method=" + flip + " ! "
					+ "videoscale ! video/x-raw, width=" + largura + ", height=" + altura + " ! " + "videoconvert name="
					+ camera.getCodigo();

			// Áudio
			if (!audioPipeline.isEmpty()) {
				basePipeline += " " + camera.getCodigo() + "source. ! " + queue + audioPipeline;
				audioPipeline = null;
			}

			mapaBasePipeline.put(camera.getCodigo(), basePipeline);

			basePipeline = null;

			Bin bin = (Bin) Gst.parseLaunch(mapaBasePipeline.get(camera.getCodigo()));

			mapaVideoComponente.put(camera.getCodigo(), new GstVideoComponent());

			if (mapaPipeline.containsKey(camera.getCodigo())) {
				Pipeline oldPipeline = mapaPipeline.get(camera.getCodigo());
				if (oldPipeline.isPlaying()) {
					oldPipeline.setState(State.NULL); // 📌 Garante que o GStreamer pare corretamente
					oldPipeline.dispose(); // Libera memória e recursos do GStreamer
					mapaPipeline.remove(camera.getCodigo()); // Remove do mapa para evitar referências pendentes
				}
			}

			mapaPipeline.put(camera.getCodigo(), new Pipeline());
			mapaPipeline.get(camera.getCodigo()).addMany(bin, mapaVideoComponente.get(camera.getCodigo()).getElement());
			Element.linkMany(bin, mapaVideoComponente.get(camera.getCodigo()).getElement());

			Element videoSink = mapaVideoComponente.get(camera.getCodigo()).getElement();
			videoSink.set("sync", false);
			mapaPipeline.get(camera.getCodigo()).add(videoSink);
			mapaPipeline.get(camera.getCodigo()).getElementByName(camera.getCodigo()).link(videoSink);

			mapaPipeline.get(camera.getCodigo()).setState(State.PAUSED);
			mapaPipeline.get(camera.getCodigo()).play();
			
			Pad sinkPad = videoSink.getStaticPad("sink");
			if (sinkPad != null) {
				mapaPad.put(camera.getCodigo(), sinkPad);
			}
						
			// Defina um intervalo de atualização para a taxa de FPS
			long fpsUpdateInterval = TimeUnit.MILLISECONDS.toNanos(1000); // Atualiza a cada 500ms
			AtomicLong lastFpsUpdateTime = new AtomicLong(System.nanoTime());

			// Variáveis de controle de tempo e contagem de frames

			mapaStartTime.put(camera.getCodigo(), -1L);
			mapaFrameCount.put(camera.getCodigo(), 0);

			// Adiciona um "probe" no Pad para calcular FPS e latência entre pacotes
			mapaPad.get(camera.getCodigo()).addProbe(PadProbeType.BUFFER, (pad, info) -> {
				Buffer buffer = info.getBuffer();
				if (buffer != null) {
					long bufferTimestamp = buffer.getPresentationTimestamp();

					// Inicializa timestamps e contagem de frames se for o primeiro frame
					mapaStartTime.putIfAbsent(camera.getCodigo(), bufferTimestamp);
					mapaFrameCount.merge(camera.getCodigo(), 1, Integer::sum);

					// Tempo decorrido desde o primeiro frame
					long elapsedTime = bufferTimestamp - mapaStartTime.get(camera.getCodigo());
					double seconds = elapsedTime / (double) TimeUnit.SECONDS.toNanos(1);

					// 🔹 Medir latência entre pacotes
					long now = System.nanoTime();
					long lastTime = mapaLastPacketTime.getOrDefault(camera.getCodigo(), now);
					long packetLatency = (now - lastTime) / 1_000_000; // Convertendo para ms
					mapaLastPacketTime.put(camera.getCodigo(), now); // Atualiza o tempo do último pacote

					// 🔹 Armazena todas as latências para cálculo da média
					mapaLatenciasPacote.computeIfAbsent(camera.getCodigo(), k -> new ArrayList<>()).add(packetLatency);

					// Se o tempo decorrido for suficiente, calcula o FPS e a latência média
					if (now - lastFpsUpdateTime.get() >= fpsUpdateInterval && seconds >= 1.0) {
						double fps = mapaFrameCount.get(camera.getCodigo()) / seconds;

						// 🔹 Calcula a média da latência entre pacotes
						List<Long> latencias = mapaLatenciasPacote.get(camera.getCodigo());
						double avgPacketLatency = latencias.stream().mapToLong(Long::longValue).average().orElse(0);

						// 🔹 Armazena FPS e latência média entre pacotes
						mapaFps.put(camera.getCodigo(), fps);
						mapaLatenciaPacote.put(camera.getCodigo(), (long) avgPacketLatency);

						// Atualiza o overlay no Swing
						SwingUtilities.invokeLater(() -> {
							try {
								String overlayText = String.format("%s - %s - FPS: %d", camera.getCodigo(),
										camera.getIp(), (int) Math.round(fps));

								VideoOverlayManager.updateOverlayText(camera, overlayText);
							} catch (NullPointerException npe) {
								npe.printStackTrace();
							}
						});

						// Reset das contagens para iniciar uma nova medição
						mapaFrameCount.put(camera.getCodigo(), 0);
						mapaStartTime.put(camera.getCodigo(), bufferTimestamp);
						lastFpsUpdateTime.set(now);

						// 🔹 Limpa a lista de latências após o cálculo
						latencias.clear();
					}
				}
				return PadProbeReturn.OK;
			});
			
			mapaInternalFrame.put(camera.getCodigo(), new JInternalFrame());
			javax.swing.plaf.basic.BasicInternalFrameUI ui = (javax.swing.plaf.basic.BasicInternalFrameUI) mapaInternalFrame
					.get(camera.getCodigo()).getUI();
			ui.setNorthPane(null); // Remove a barra de título
			mapaInternalFrame.get(camera.getCodigo()).setBorder(BorderFactory.createLineBorder(Color.BLACK, -1)); // Remove
	
			SwingUtilities.invokeLater(() -> {
				JInternalFrame frame = mapaInternalFrame.get(camera.getCodigo());
				if (frame != null) {
					frame.getContentPane().removeAll();
					VideoOverlayManager.addOverlay(camera, mapaInternalFrame.get(camera.getCodigo()),
							mapaVideoComponente.get(camera.getCodigo()),
							String.format("%s - %s - FPS: 0", camera.getCodigo(), camera.getIp()));
					frame.getContentPane().revalidate();
					frame.getContentPane().repaint();
				}
			});

			// inicializar Janela Câmera
			try {
				this.configurarJanela(camera);
			} catch (Exception e) {
				logger.error("Erro ao configurar janela para câmera: " + camera.getCodigo(), e);
				aplicarFallbackGrade(mapaInternalFrame.get(camera.getCodigo()), camera);
			}

			mapaInternalFrame.get(camera.getCodigo())
					.setFrameIcon(new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE)));
			layeredPane.add(mapaInternalFrame.get(camera.getCodigo()), JLayeredPane.PALETTE_LAYER);

			String status = this.getStatusPorCamera(mapaPipeline.get(camera.getCodigo()), camera.getCodigo());

			if (mapaCameras.get(camera.getCodigo()).getPTZ() == 1 && status.equals("01")) {

				try {

					mapaOnvifDevice.put(camera.getCodigo(),
							new OnvifDevice(camera.getIp(), camera.getUsuario(), camera.getSenha()));
					String profileName = mapaOnvifDevice.get(camera.getCodigo()).getDevices().getProfiles().get(0)
							.getName();
					logger.info("Conexão bem-sucedida com a câmera ONVIF: " + camera.getCodigo() + ", Perfil: "
							+ profileName);

					mapaPTZOverlayPanel.put(camera.getCodigo(),
							new PTZOverlayPanel(mapaOnvifDevice.get(camera.getCodigo()), this, camera));

					if (mapaInternalFrame.get(camera.getCodigo()) != null
							&& mapaInternalFrame.get(camera.getCodigo()) != null) {
						mapaInternalFrame.get(camera.getCodigo())
								.setGlassPane(mapaPTZOverlayPanel.get(camera.getCodigo()));
					} else {
						// Log ou tratamento de erro caso internalFrame ou o índice estejam nulos
						logger.error("internalFrame ou índice estão nulos em PTZ.");
						return;
					}

					mapaPTZOverlayPanel.get(camera.getCodigo()).setVisible(false);

					mapaInternalFrame.get(camera.getCodigo()).addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							mapaPTZOverlayPanel.get(camera.getCodigo()).setVisible(true);
						}

						@Override
						public void mouseExited(MouseEvent e) {
							mapaPTZOverlayPanel.get(camera.getCodigo()).setVisible(false);
						}
					});

				} catch (IndexOutOfBoundsException e) {
					logger.error("Erro de índice ao acessar o perfil da câmera: " + camera.getCodigo()
							+ ". Verifique se a câmera possui perfis ONVIF configurados. Detalhes: " + e);
				} catch (ConnectException e) {
					logger.error("Erro de conexão ao acessar a câmera ONVIF com IP: " + camera.getIp()
							+ ". Verifique a conectividade de rede e as configurações da câmera. Detalhes: " + e);
				} catch (SOAPException e) {
					logger.error("Erro de comunicação SOAP ao acessar a câmera ONVIF com IP: " + camera.getIp()
							+ ". Verifique as credenciais ou a compatibilidade ONVIF. Detalhes: " + e);
				} catch (Exception e) {
					// Captura qualquer outra exceção não prevista
					logger.error("Erro inesperado ao acessar a câmera ONVIF " + camera.getCodigo() + ". Detalhes: " + e,
							e);
				}

			}

			if (mapaPTZOverlayPanel.get(camera.getCodigo()) == null) {

				try {
					// Passando a instância correta do Viewer (this) e o index da câmera
					mapaRotationOverlayPanel.put(camera.getCodigo(), new RotationOverlayPanel(this, camera));

					if (mapaInternalFrame.get(camera.getCodigo()) != null) {
						mapaInternalFrame.get(camera.getCodigo())
								.setGlassPane(mapaRotationOverlayPanel.get(camera.getCodigo()));
					} else {
						// Log ou tratamento de erro caso internalFrame ou o índice estejam nulos
						logger.error("internalFrame ou índice estão nulos");
						return;
					}

					// internalFrame[index].setGlassPane(rotationOverlayPanel[index]); // Adiciona o
					// painel de rotação

					// Define que o painel de rotação começa invisível
					mapaRotationOverlayPanel.get(camera.getCodigo()).setVisible(false);

					// Adiciona eventos de mouse para mostrar/ocultar o painel de rotação
					mapaInternalFrame.get(camera.getCodigo()).addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							mapaRotationOverlayPanel.get(camera.getCodigo()).setVisible(true); // Mostra o painel quando
																								// o mouse entra
						}

						@Override
						public void mouseExited(MouseEvent e) {
							mapaRotationOverlayPanel.get(camera.getCodigo()).setVisible(false); // Oculta o painel
																								// quando o mouse sai
						}
					});

				} catch (IndexOutOfBoundsException e) {
					logger.error("Erro de índice ao acessar o perfil da câmera: " + camera.getCodigo()
							+ ". Verifique se a câmera possui perfis ONVIF configurados. Detalhes: " + e);
				} catch (Exception e) {
					logger.error("Erro inesperado ao acessar a câmera ONVIF " + camera.getCodigo() + ". Detalhes: " + e,
							e);
				}

			}

			mapaInternalFrame.get(camera.getCodigo()).addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int mouseX = e.getX();
					int mouseY = e.getY();

					if (mouseX <= RESIZE_MARGIN && mouseY <= RESIZE_MARGIN) {
						resizeDirection = "TOP_LEFT";
					} else if (mouseX >= mapaInternalFrame.get(camera.getCodigo()).getWidth() - RESIZE_MARGIN
							&& mouseY <= RESIZE_MARGIN) {
						resizeDirection = "TOP_RIGHT";
					} else if (mouseX <= RESIZE_MARGIN
							&& mouseY >= mapaInternalFrame.get(camera.getCodigo()).getHeight() - RESIZE_MARGIN) {
						resizeDirection = "BOTTOM_LEFT";
					} else if (mouseX >= mapaInternalFrame.get(camera.getCodigo()).getWidth() - RESIZE_MARGIN
							&& mouseY >= mapaInternalFrame.get(camera.getCodigo()).getHeight() - RESIZE_MARGIN) {
						resizeDirection = "BOTTOM_RIGHT";
					} else if (mouseX <= RESIZE_MARGIN) {
						resizeDirection = "LEFT";
					} else if (mouseX >= mapaInternalFrame.get(camera.getCodigo()).getWidth() - RESIZE_MARGIN) {
						resizeDirection = "RIGHT";
					} else if (mouseY <= RESIZE_MARGIN) {
						resizeDirection = "TOP";
					} else if (mouseY >= mapaInternalFrame.get(camera.getCodigo()).getHeight() - RESIZE_MARGIN) {
						resizeDirection = "BOTTOM";
					} else {
						// Nenhuma borda tocada, habilita movimentação
						resizeDirection = null;
						mapaLastLocation.put(camera.getCodigo(), e.getLocationOnScreen());
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					resizeDirection = null; // Desativa o redimensionamento ao soltar o mouse
					memorizeButton.setIcon(ICON_FIXAR_OFF);
				}
			});

			mapaInternalFrame.get(camera.getCodigo()).addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if (resizeDirection != null) {
						// Redimensionamento da janela
						int deltaX = e.getX();
						int deltaY = e.getY();

						int newWidth = mapaInternalFrame.get(camera.getCodigo()).getWidth();
						int newHeight = mapaInternalFrame.get(camera.getCodigo()).getHeight();
						int newX = mapaInternalFrame.get(camera.getCodigo()).getX();
						int newY = mapaInternalFrame.get(camera.getCodigo()).getY();

						switch (resizeDirection) {
						case "LEFT":
							newWidth -= deltaX;
							newX += deltaX;
							break;
						case "RIGHT":
							newWidth = deltaX;
							break;
						case "TOP":
							newHeight -= deltaY;
							newY += deltaY;
							break;
						case "BOTTOM":
							newHeight = deltaY;
							break;
						case "TOP_LEFT":
							newWidth -= deltaX;
							newX += deltaX;
							newHeight -= deltaY;
							newY += deltaY;
							break;
						case "TOP_RIGHT":
							newWidth = deltaX;
							newHeight -= deltaY;
							newY += deltaY;
							break;
						case "BOTTOM_LEFT":
							newWidth -= deltaX;
							newX += deltaX;
							newHeight = deltaY;
							break;
						case "BOTTOM_RIGHT":
							newWidth = deltaX;
							newHeight = deltaY;
							break;
						}

						// Define limites mínimos para largura e altura
						if (newWidth >= 100) {
							mapaInternalFrame.get(camera.getCodigo()).setSize(newWidth,
									mapaInternalFrame.get(camera.getCodigo()).getHeight());
							mapaInternalFrame.get(camera.getCodigo()).setLocation(newX,
									mapaInternalFrame.get(camera.getCodigo()).getY());
						}
						if (newHeight >= 100) {
							mapaInternalFrame.get(camera.getCodigo())
									.setSize(mapaInternalFrame.get(camera.getCodigo()).getWidth(), newHeight);
							mapaInternalFrame.get(camera.getCodigo())
									.setLocation(mapaInternalFrame.get(camera.getCodigo()).getX(), newY);
						}
					} else {
						// MOVIMENTAÇÃO DA JANELA
						Point newPoint = e.getLocationOnScreen();
						Point lastPoint = mapaLastLocation.get(camera.getCodigo());

						if (lastPoint != null) {
							int xDiff = newPoint.x - lastPoint.x;
							int yDiff = newPoint.y - lastPoint.y;

							Point frameLocation = mapaInternalFrame.get(camera.getCodigo()).getLocation();
							frameLocation.translate(xDiff, yDiff);

							mapaInternalFrame.get(camera.getCodigo()).setLocation(frameLocation);
						}

						// Atualiza a última posição do mouse para futuras movimentações
						mapaLastLocation.put(camera.getCodigo(), newPoint);
					}
				}

				@Override
				public void mouseMoved(MouseEvent e) {
					// Ajusta o cursor com base na borda/canto
					int mouseX = e.getX();
					int mouseY = e.getY();

					if (mouseX <= RESIZE_MARGIN && mouseY <= RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					} else if (mouseX >= mapaInternalFrame.get(camera.getCodigo()).getWidth() - RESIZE_MARGIN
							&& mouseY <= RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					} else if (mouseX <= RESIZE_MARGIN
							&& mouseY >= mapaInternalFrame.get(camera.getCodigo()).getHeight() - RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
					} else if (mouseX >= mapaInternalFrame.get(camera.getCodigo()).getWidth() - RESIZE_MARGIN
							&& mouseY >= mapaInternalFrame.get(camera.getCodigo()).getHeight() - RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					} else if (mouseX <= RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
					} else if (mouseX >= mapaInternalFrame.get(camera.getCodigo()).getWidth() - RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					} else if (mouseY <= RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
					} else if (mouseY >= mapaInternalFrame.get(camera.getCodigo()).getHeight() - RESIZE_MARGIN) {
						mapaInternalFrame.get(camera.getCodigo())
								.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
					} else {
						mapaInternalFrame.get(camera.getCodigo()).setCursor(Cursor.getDefaultCursor());
					}
				}
			});

			mapaInternalFrame.get(camera.getCodigo()).setVisible(true);

			setupBusErrorHandling(camera);

		});

	}
	
	private void cleanupCameraResources(String codigo) {
		// 1. Pipeline (GStreamer)
		Pipeline pipeline = mapaPipeline.remove(codigo);
		if (pipeline != null) {
			try {
				pipeline.setState(State.NULL);
			} catch (Exception e) {
				logger.warn("Erro ao setar pipeline NULL para câmera: " + codigo, e);
			}
			try {
				pipeline.dispose();
			} catch (Exception e) {
				logger.warn("Erro ao descartar pipeline da câmera: " + codigo, e);
			}
		}

		// 2. VideoComponent
		GstVideoComponent comp = mapaVideoComponente.remove(codigo);
		if (comp != null) {
		    try {
		        // 🔹 REMOVE o componente da UI
		        Container parent = comp.getParent();
		        if (parent != null) {
		            parent.remove(comp);
		            parent.revalidate();
		            parent.repaint();
		        }

		        // 🔹 Libera recursos GStreamer do sink
		        Element element = comp.getElement();
		        if (element != null) {
		            element.setState(State.NULL);
		            element.dispose(); // 🔥 Libera recursos nativos do sink
		        }

		    } catch (Exception e) {
		        logger.warn("Erro ao descartar GstVideoComponent da câmera: " + codigo, e);
		    }
		}


		// 3. InternalFrame
		JInternalFrame frame = mapaInternalFrame.remove(codigo);
		if (frame != null) {
			try {
				// Remove listeners se necessário
				for (MouseListener l : frame.getMouseListeners())
					frame.removeMouseListener(l);
				for (MouseMotionListener ml : frame.getMouseMotionListeners())
					frame.removeMouseMotionListener(ml);

				frame.getContentPane().removeAll();
				frame.dispose();
			} catch (Exception e) {
				logger.warn("Erro ao descartar JInternalFrame da câmera: " + codigo, e);
			}
		}

		// 4. Overlays
		mapaPTZOverlayPanel.remove(codigo);
		mapaRotationOverlayPanel.remove(codigo);

		// 5. Remoção segura de Strings pesadas
		mapaBasePipeline.remove(codigo);

		// 6. Recursos de cálculo de FPS e latência
		mapaPad.remove(codigo);
		mapaFps.remove(codigo);
		mapaStartTime.remove(codigo);
		mapaFrameCount.remove(codigo);
		mapaLastPacketTime.remove(codigo);
		List<Long> latencias = mapaLatenciasPacote.remove(codigo);
		if (latencias != null)
			latencias.clear();
		mapaLatenciaPacote.remove(codigo);

		// 7. ONVIF e posição
		mapaOnvifDevice.remove(codigo);
		mapaLastLocation.remove(codigo);

		logger.info("Recursos da câmera [{}] foram limpos com sucesso.", codigo);
	}

	private void setupBusErrorHandling(CameraDTO camera) {
	    final String camKey = camera.getCodigo();

	    synchronized (lock) {
	        Pipeline pipeline = mapaPipeline.get(camKey);
	        if (pipeline == null) {
	            logger.error("Pipeline não encontrado ao configurar Bus para a câmera {}", camKey);
	            return;
	        }

	        Bus bus;
	        try {
	            bus = pipeline.getBus();
	        } catch (Exception e) {
	            logger.error("Falha ao obter Bus para a câmera {}: {}", camKey, e.getMessage(), e);
	            return;
	        }
	        if (bus == null) {
	            logger.error("Bus é nulo para a câmera {}, abortando configuração", camKey);
	            return;
	        }

	        // Desconecta ERROR handler anterior (se houver) para evitar duplicidade
	        Bus.ERROR prevErrorHandler = mapaBusErrorHandlers.remove(camKey);
	        if (prevErrorHandler != null) {
	            try { bus.disconnect(prevErrorHandler); } catch (Exception ignore) {}
	        }

	        final Bus.ERROR errorHandler = new Bus.ERROR() {
	            @Override
	            public void errorMessage(GstObject source, int code, String message) {
	                try { // <- BLINDA TUDO
	                    synchronized (lock) {
	                        logger.warn("Erro no Bus da câmera {} (code={}): {}", camKey, code, message);

	                        // Evita reentrância: se já tem teardown/reconnect em curso, sai
	                        if (!beginReconnect(camKey)) {
	                            return;
	                        }
	                        try {
	                            // Para e remove timer antigo
	                            javax.swing.Timer oldTimer = mapaReconnectTimers.remove(camKey);
	                            if (oldTimer != null) {
	                                try { oldTimer.stop(); } catch (Exception ignore) {}
	                            }

	                            // Captura pipeline atual; se já não existe, só agenda reconexão
	                            Pipeline oldPipeline = mapaPipeline.get(camKey);
	                            if (oldPipeline == null) {
	                                safeAlert(camKey, camera.getIp());
	                                scheduleReconnectBackoff(camKey, camera); // backoff
	                                return;
	                            }

	                            // Remove do mapa para impedir reuso enquanto finalizamos
	                            mapaPipeline.remove(camKey);

	                            // Desconecta handlers antigos do BUS desse pipeline
	                            Bus oldBus = null;
	                            try { oldBus = oldPipeline.getBus(); } catch (Exception ignore) {}
	                            Bus.STATE_CHANGED prevStateHandler = mapaBusStateHandlers.remove(camKey);
	                            if (oldBus != null && prevStateHandler != null) {
	                                try { oldBus.disconnect(prevStateHandler); } catch (Exception ignore) {}
	                            }
	                            Bus.ERROR prevErr = mapaBusErrorHandlers.remove(camKey);
	                            if (oldBus != null && prevErr != null) {
	                                try { oldBus.disconnect(prevErr); } catch (Exception ignore) {}
	                            }

	                            // Conecta um STATE_CHANGED para dar dispose quando atingir NULL
	                            Bus.STATE_CHANGED stateHandler = new Bus.STATE_CHANGED() {
	                                @Override
	                                public void stateChanged(GstObject src, State old, State newState, State pending) {
	                                    if (newState == State.NULL) {
	                                        try { oldPipeline.dispose(); } catch (Exception ignore) {}
	                                    }
	                                }
	                            };
	                            if (oldBus != null) {
	                                try {
	                                    oldBus.connect(stateHandler);
	                                    mapaBusStateHandlers.put(camKey, stateHandler);
	                                } catch (Exception e) {
	                                    logger.debug("Falha ao conectar STATE_CHANGED em oldBus {}: {}", camKey, e.getMessage());
	                                }
	                            }

	                            // Tenta transitar para NULL; se der erro, faz dispose direto
	                            try {
	                                oldPipeline.setState(State.NULL);
	                            } catch (Throwable t) {
	                                try { oldPipeline.dispose(); } catch (Exception ignore) {}
	                            }

	                            // Limpa métricas/estruturas auxiliares
	                            mapaPad.remove(camKey);
	                            mapaFps.remove(camKey);
	                            mapaFrameCount.remove(camKey);
	                            mapaStartTime.remove(camKey);
	                            mapaLastPacketTime.remove(camKey);
	                            mapaLatenciaPacote.remove(camKey);
	                            mapaLatenciasPacote.remove(camKey);

	                            // Alerta (safe) e agenda reconexão com backoff
	                            safeAlert(camKey, camera.getIp());
	                            scheduleReconnectBackoff(camKey, camera);

	                        } finally {
	                            endReconnect(camKey);
	                        }
	                    }
	                } catch (Throwable t) {
	                    // NADA sai daqui
	                    logger.error("Falha no errorHandler {}: {}", camKey, t.toString(), t);
	                }
	            }

	            private void safeAlert(String camKey, String ip) {
	                try {
	                    var frame = mapaInternalFrame.get(camKey);
	                    if (frame != null) {
	                        AlertDisplay.displayAlert(frame, camKey, ip);
	                    }
	                } catch (Throwable ignore) {}
	            }
	        };

	        try {
	            bus.connect(errorHandler);
	            mapaBusErrorHandlers.put(camKey, errorHandler);
	        } catch (Exception e) {
	            logger.error("Falha ao conectar errorHandler no Bus da câmera {}: {}", camKey, e.getMessage(), e);
	        }
	    }
	}

	private boolean beginReconnect(String key) {
	    reconnecting.putIfAbsent(key, new java.util.concurrent.atomic.AtomicBoolean(false));
	    return reconnecting.get(key).compareAndSet(false, true);
	}
	private void endReconnect(String key) {
	    var f = reconnecting.get(key);
	    if (f != null) f.set(false);
	}
	
	private void scheduleReconnectBackoff(String camKey, CameraDTO camera) {
	    long now = System.currentTimeMillis();
	    int attempt = retryAttempts.getOrDefault(camKey, 0);
	    long delay = Math.min(MAX_DELAY_MS, (long)(BASE_DELAY_MS * Math.pow(2, attempt)));
	    long when  = Math.max(now + delay, nextRetryAt.getOrDefault(camKey, 0L));

	    nextRetryAt.put(camKey, when);
	    retryAttempts.put(camKey, attempt + 1);

	    int ms = (int)Math.max(0, when - now);
	    javax.swing.Timer t = new javax.swing.Timer(ms, e -> {
	        // rode sua rotina num executor (ideal) ou numa thread
	        new Thread(() -> {
	            try {
	                handleReconnection(camera);
	                // se reconectar OK, zera backoff
	                retryAttempts.remove(camKey);
	                nextRetryAt.remove(camKey);
	            } catch (Throwable ex) {
	                // mantém contadores para próximo backoff
	                logger.warn("handleReconnection falhou em {}: {}", camKey, ex.toString());
	            }
	        }).start();
	    });
	    t.setRepeats(false);
	    // substitui timer antigo, se houver
	    javax.swing.Timer old = mapaReconnectTimers.put(camKey, t);
	    if (old != null) try { old.stop(); } catch (Exception ignore) {}
	    t.start();
	}


	private void handleReconnection(CameraDTO camera) {
		// 🔹 Verifica se já há uma tentativa de reconexão ativa
		if (mapaIsReconnecting.getOrDefault(camera.getCodigo(), false)) {
			logger.info("Tentativa de reconexão já em andamento para a câmera {}", camera.getCodigo());
			Timer timer = mapaReconnectTimers.remove(camera.getCodigo());
			if (timer != null) {
				timer.stop();
				logger.info("ReconnectTimer para a câmera {} interrompido e removido do mapa.", camera.getCodigo());
			}
			return;
		}

		// 🔹 Verifica se o executor está disponível antes de continuar
		if (executor == null || executor.isShutdown()) {
			logger.warn("Executor indisponível, não é possível reconectar a câmera {}", camera.getCodigo());
			Timer timer = mapaReconnectTimers.remove(camera.getCodigo());
			if (timer != null) {
				timer.stop();
				logger.info("ReconnectTimer para a câmera {} interrompido e removido do mapa.", camera.getCodigo());
			}
			return;
		}

		// 🔹 Marca que a câmera está em processo de reconexão
		mapaIsReconnecting.put(camera.getCodigo(), true);

		executor.submit(() -> {

			Thread.currentThread().setName("Reconnect-" + camera.getCodigo());

			logger.info("{} Tentando re-conectar...", camera.getCodigo());

			// 🔁 Limpa recursos antigos relacionados à câmera antes de criar novo pipeline
			mapaPad.remove(camera.getCodigo());
			mapaFps.remove(camera.getCodigo());
			mapaFrameCount.remove(camera.getCodigo());
			mapaStartTime.remove(camera.getCodigo());
			mapaLastPacketTime.remove(camera.getCodigo());
			mapaLatenciaPacote.remove(camera.getCodigo());
			mapaLatenciasPacote.remove(camera.getCodigo());

			try {
				// Se já estiver rodando, não precisa criar um novo pipeline
				Pipeline pipe = mapaPipeline.get(camera.getCodigo());
				if (pipe != null && pipe.getState() == State.PLAYING) {
					logger.info("{} já está em execução. Ignorando reconexão.", camera.getCodigo());
					return;
				}

				Pipeline oldPipeline = mapaPipeline.remove(camera.getCodigo());
				if (oldPipeline != null) {
					try {
						oldPipeline.setState(State.NULL);
						int retries = 0;
						while (oldPipeline.getState() != State.NULL && retries++ < 30) {
							Thread.sleep(100);
						}

						Bus bus = oldPipeline.getBus();
						if (bus != null) {
							var errorHandler = mapaBusErrorHandlers.remove(camera.getCodigo());
							if (errorHandler != null)
								bus.disconnect(errorHandler);

							var stateHandler = mapaBusStateHandlers.remove(camera.getCodigo());
							if (stateHandler != null)
								bus.disconnect(stateHandler);

							var reconnectHandler = mapaBusReconnectHandlers.remove(camera.getCodigo());
							if (reconnectHandler != null)
								bus.disconnect(reconnectHandler);
						}

						oldPipeline.dispose();
						logger.info("Pipeline anterior da câmera {} foi corretamente descartado", camera.getCodigo());

					} catch (Exception e) {
						logger.warn("Erro ao destruir pipeline anterior da câmera {}: {}", camera.getCodigo(),
								e.getMessage());
					}
				}

				Bin bin = (Bin) Gst.parseLaunch(mapaBasePipeline.get(camera.getCodigo()));
				mapaVideoComponente.put(camera.getCodigo(), new GstVideoComponent());

				mapaPipeline.put(camera.getCodigo(), new Pipeline());
				mapaPipeline.get(camera.getCodigo()).addMany(bin,
						mapaVideoComponente.get(camera.getCodigo()).getElement());

				Element videoSink = mapaVideoComponente.get(camera.getCodigo()).getElement();
				videoSink.set("sync", false);
				mapaPipeline.get(camera.getCodigo()).add(videoSink);
				mapaPipeline.get(camera.getCodigo()).getElementByName(camera.getCodigo()).link(videoSink);

				mapaPipeline.get(camera.getCodigo()).play();

				Bus bus = mapaPipeline.get(camera.getCodigo()).getBus();
				if (bus != null) {
					var oldReconnectHandler = mapaBusReconnectHandlers.remove(camera.getCodigo());
					if (oldReconnectHandler != null) {
						bus.disconnect(oldReconnectHandler);
						logger.debug("Handler de reconexão antigo desconectado da câmera {}", camera.getCodigo());
					}
				}

				Pad sinkPad = videoSink.getStaticPad("sink");
				if (sinkPad != null) {
					mapaPad.put(camera.getCodigo(), sinkPad);
				}

				// Defina um intervalo de atualização para a taxa de FPS
				long fpsUpdateInterval = TimeUnit.MILLISECONDS.toNanos(1000);
				AtomicLong lastFpsUpdateTime = new AtomicLong(System.nanoTime());

				mapaStartTime.put(camera.getCodigo(), -1L);
				mapaFrameCount.put(camera.getCodigo(), 0);

				mapaPad.get(camera.getCodigo()).addProbe(PadProbeType.BUFFER, (pad, info) -> {
					Buffer buffer = info.getBuffer();
					if (buffer != null) {
						long bufferTimestamp = buffer.getPresentationTimestamp();

						mapaStartTime.putIfAbsent(camera.getCodigo(), bufferTimestamp);
						mapaFrameCount.merge(camera.getCodigo(), 1, Integer::sum);

						long elapsedTime = bufferTimestamp - mapaStartTime.get(camera.getCodigo());
						double seconds = elapsedTime / (double) TimeUnit.SECONDS.toNanos(1);

						long now = System.nanoTime();
						long lastTime = mapaLastPacketTime.getOrDefault(camera.getCodigo(), now);
						long packetLatency = (now - lastTime) / 1_000_000;
						mapaLastPacketTime.put(camera.getCodigo(), now);

						mapaLatenciasPacote.computeIfAbsent(camera.getCodigo(), k -> new ArrayList<>())
								.add(packetLatency);

						if (now - lastFpsUpdateTime.get() >= fpsUpdateInterval && seconds >= 1.0) {
							double fps = mapaFrameCount.get(camera.getCodigo()) / seconds;

							List<Long> latencias = mapaLatenciasPacote.get(camera.getCodigo());
							double avgPacketLatency = latencias.stream().mapToLong(Long::longValue).average().orElse(0);

							mapaFps.put(camera.getCodigo(), fps);
							mapaLatenciaPacote.put(camera.getCodigo(), (long) avgPacketLatency);

							SwingUtilities.invokeLater(() -> {
								try {
									String overlayText = String.format("%s - %s - FPS: %d", camera.getCodigo(),
											camera.getIp(), (int) Math.round(fps));
									VideoOverlayManager.updateOverlayText(camera, overlayText);
								} catch (NullPointerException npe) {
									npe.printStackTrace();
								}
							});

							mapaFrameCount.put(camera.getCodigo(), 0);
							mapaStartTime.put(camera.getCodigo(), bufferTimestamp);
							lastFpsUpdateTime.set(now);
							latencias.clear();
						}
					}
					return PadProbeReturn.OK;
				});

				if (mapaPipeline.get(camera.getCodigo()).getState() == State.PLAYING) {
					logger.info("{} Conectou...", camera.getCodigo());
					AlertDisplay.resetTimer(camera.getCodigo());

					SwingUtilities.invokeLater(() -> {
						JInternalFrame frame = mapaInternalFrame.get(camera.getCodigo());
						if (frame != null) {
							frame.getContentPane().removeAll();
							VideoOverlayManager.addOverlay(camera, frame, mapaVideoComponente.get(camera.getCodigo()),
									String.format("%s - %s - FPS: 0", camera.getCodigo(), camera.getIp()));
							frame.getContentPane().revalidate();
							frame.getContentPane().repaint();
						}
					});

					Timer reconnectTimer = mapaReconnectTimers.remove(camera.getCodigo());
					if (reconnectTimer != null) {
						reconnectTimer.stop();
					}
				}

				bus = mapaPipeline.get(camera.getCodigo()).getBus();
				if (bus != null) {
					var oldReconnectHandler = mapaBusReconnectHandlers.remove(camera.getCodigo());
					if (oldReconnectHandler != null) {
						bus.disconnect(oldReconnectHandler);
						logger.debug("Handler de reconexão antigo desconectado da câmera {}", camera.getCodigo());
					}
				}

				setupBusErrorHandling(camera);

			} finally {
				mapaIsReconnecting.put(camera.getCodigo(), false);
				logger.debug("Finalizando reconexão para {}", camera.getCodigo());
			}
		});
	}

	private void reconnectToErrorBus(CameraDTO camera) {

		Bus bus = mapaPipeline.get(camera.getCodigo()).getBus();
		Bus.ERROR reconnectErrorHandler = new Bus.ERROR() {
			public void errorMessage(GstObject source, int code, String message) {
				AlertDisplay.displayAlert(mapaInternalFrame.get(camera.getCodigo()), camera.getCodigo(),
						camera.getIp());

				Timer timer = mapaReconnectTimers.get(camera.getCodigo());
				if (timer != null) {
					timer.restart();
					mapaReconnectTimers.put(camera.getCodigo(), timer);
				}
			}
		};

		bus.connect(reconnectErrorHandler);
		mapaBusReconnectHandlers.put(camera.getCodigo(), reconnectErrorHandler);

	}

	private void exibirAvisoMonitorAusente(String cameraCodigo, ConfiguracaoVisualDTO configuracao) {
		String mensagem = String
				.format("A posição configurada para a câmera '%s' não está dentro da área visível dos monitores.%n"
						+ "A janela será posicionada automaticamente.", cameraCodigo);

		// Loga o aviso
		logger.warn(mensagem);

		// Mostra mensagem ao usuário
		SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, mensagem, "Aviso: Monitor Ausente",
				JOptionPane.WARNING_MESSAGE));
	}

	private void configurarJanela(CameraDTO camera) {

		if (camera == null) {
			logger.warn("Câmera não encontrada.");
			return;
		}

		ConfiguracaoVisualDTO configuracaoVisualDTO = this.getConfiguracaoVisualByCodigo(camera.getCodigo());
		JInternalFrame frame = mapaInternalFrame.get(camera.getCodigo());

		if (configuracaoVisualDTO != null) {
			aplicarConfiguracaoVisual(frame, configuracaoVisualDTO);
		} else {
			aplicarFallbackGrade(frame, camera);
		}
	}

	private void aplicarConfiguracaoVisual(JInternalFrame frame, ConfiguracaoVisualDTO configuracao) {
		frame.setLocation(configuracao.getPosicaoHorizontal(), configuracao.getPosicaoVertical());
		frame.setSize(configuracao.getTamanhoHorizontal(), configuracao.getTamanhoVertical());
	}

	private void aplicarFallbackGrade(JInternalFrame frame, CameraDTO camera) {
		int totalColunas = 4; // Número de colunas na grade
		int larguraJanela = 480; // Largura padrão da janela
		int alturaJanela = 270; // Altura padrão da janela

		int index = new ArrayList<>(mapaCameras.keySet()).indexOf(camera.getCodigo());
		if (index == -1) {
			logger.warn("Câmera {} não encontrada no mapaCameras para fallback.", camera.getCodigo());
			return;
		}

		int coluna = index % totalColunas; // Determina a coluna
		int linha = index / totalColunas; // Determina a linha

		frame.setSize(larguraJanela, alturaJanela);
		frame.setLocation(coluna * larguraJanela, linha * alturaJanela);

		logger.info("Fallback aplicado para câmera {} na posição ({}, {}).", camera.getCodigo(), coluna, linha);
	}

	public ConfiguracaoVisualDTO getConfiguracaoVisualByCodigo(String codigoCamera) {
		if (configuracaoVisual == null) {
			
			
			String scenarioPrefix = "";

			for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
			    String name  = e.getKey();
			    Boolean fixed = e.getValue();

			    if (Boolean.TRUE.equals(fixed)) {
			        scenarioPrefix = name.toLowerCase(Locale.ROOT);
			        break; 
			    }
			}
						
			if (!scenarioPrefix.isEmpty()) {
				configuracaoVisual = loadVisualScenarioConfigurations(scenarioPrefix);
			} else {
				configuracaoVisual = loadConfigurationsFromVisual();
			}
		}

		for (ConfiguracaoVisualDTO configuracaoVisualDTO : configuracaoVisual) {
			if (configuracaoVisualDTO.getCodigoCamera().equals(codigoCamera)) {
				return configuracaoVisualDTO; // Retorna o CameraDTO correspondente ao código
			}
		}
		return null;
	}

	private void limparGridFree() {
		logger.info("Desativando o modo Grid Free...");

		if (mapaCameras == null || mapaCameras.isEmpty()) {
			return;
		}

		// Remove componentes do LayeredPane
		for (Component comp : layeredPane.getComponents()) {
			if (layeredPane.getLayer(comp) == JLayeredPane.PALETTE_LAYER) {

				// Remove listeners explicitamente
				for (ComponentListener listener : comp.getComponentListeners()) {
					comp.removeComponentListener(listener);
				}
				for (MouseListener listener : comp.getMouseListeners()) {
					comp.removeMouseListener(listener);
				}
				for (MouseMotionListener listener : comp.getMouseMotionListeners()) {
					comp.removeMouseMotionListener(listener);
				}
				for (KeyListener listener : comp.getKeyListeners()) {
					comp.removeKeyListener(listener);
				}

				if (comp instanceof JInternalFrame) {
					JInternalFrame frame = (JInternalFrame) comp;

					if (!mapaWebViewFrames.containsValue(frame) && !mapaVNCFrames.containsValue(frame)) {
						layeredPane.remove(frame); // Primeiro remove
						frame.dispose(); // Depois libera recursos
					}

				} else {
					layeredPane.remove(comp);
				}
			}
		}

		// Remove e libera recursos associados às câmeras
		for (String codigo : mapaCameras.keySet()) {

			// Finaliza e remove pipeline
			Pipeline pipeline = mapaPipeline.get(codigo);
			if (pipeline != null) {
				logger.info("Parando e liberando pipeline da câmera {}", codigo);

				pipeline.setState(State.NULL);

				// Aguarda até 5 segundos para confirmar a parada
				try {
					pipeline.getState(5, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.warn("Timeout ao aguardar pipeline NULL para câmera {}", codigo);
				}

				pipeline.dispose();
				mapaPipeline.remove(codigo);
			}

			// Remove componentes de vídeo
			JPanel panel = mapaVideoPanel.get(codigo);
			if (panel != null) {
				for (Component child : panel.getComponents()) {
					if (child instanceof GstVideoComponent) {
						panel.remove(child); // Remove explicitamente o componente GStreamer
					}
				}
				panel.removeAll(); // Garante que todos os filhos saiam
				mapaVideoPanel.remove(codigo);
			}

			// Remove frame da câmera
			try {
				JInternalFrame frame = mapaInternalFrame.get(codigo);
				if (frame != null) {
					layeredPane.remove(frame);
					frame.dispose();
				}
				mapaInternalFrame.remove(codigo);
			} catch (Exception e) {
				logger.warn("Erro ao remover frame da câmera {}", codigo, e);
			}
		}

		// Limpa os mapas
		mapaPipeline.clear();
		mapaFps.clear();
		mapaLatenciaPacote.clear();
		mapaCameras.clear();

		// Atualiza a interface apenas uma vez
		SwingUtilities.invokeLater(() -> {
			layeredPane.revalidate();
			layeredPane.repaint();
		});

		logger.info("Grid Free desativado.");
	}

	private JPanel createButtonPanel(String name) {
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
	    buttonPanel.setOpaque(false);

	    JButton button = new JButton();
	    button.setPreferredSize(new Dimension(22, 22));
	    button.setBorder(BorderFactory.createEmptyBorder());
	    button.setContentAreaFilled(false);
	    button.setIcon(ICON_CENARIO_OFF);

	    JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
	    textPanel.setOpaque(false);

	    JLabel label = new JLabel(name);
	    label.setFont(new Font("Roboto", Font.PLAIN, 12));
	    label.setForeground(Color.GRAY);

	    // adicione ANTES de registrar listeners
	    buttons.add(button);
	    labels.add(label);
	    final int myIndex = buttons.size() - 1;

	    // clique → aciona seleção
	    button.addActionListener(e -> {
	        if (!"Aguarde...".equals(gridFreeLabel.getText())) {
	           this.simulateButtonClick(button, name.toLowerCase());
	        }
	    });

	    // hover só se não for o selecionado
	    button.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            if (myIndex != selectedIndex) {
	                button.setIcon(ICON_CENARIO_HOVER);
	                label.setForeground(Color.WHITE);
	            }
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            if (myIndex != selectedIndex) {
	                button.setIcon(ICON_CENARIO_OFF);
	                label.setForeground(Color.GRAY);
	            }
	        }
	    });

	    buttonPanel.add(button);
	    textPanel.add(label);

	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	    panel.setOpaque(false);
	    panel.add(buttonPanel);
	    panel.add(textPanel);
	    return panel;
	}


	private void processarConfiguracoesPTZ(Map<String, Integer> cameraStates) {

		// Remove linhas abaixo do cabeçalho no arquivo de configuração
		removerLinhasAbaixoDoCabecalhoPTZ();

		// Itera sobre os estados das câmeras para gravar no arquivo
		for (Map.Entry<String, Integer> entry : cameraStates.entrySet()) {
			String ptzName = entry.getKey();
			int state = entry.getValue();

			// Cria o objeto de configuração
			ConfiguracaoVisualPtzDTO configuracaoVisualPtzDTO = new ConfiguracaoVisualPtzDTO();
			configuracaoVisualPtzDTO.setCodigoPTZ(ptzName);
			configuracaoVisualPtzDTO.setEstado(state);

			// Grava a configuração no arquivo
			gravarArquivoConfiguracaoVisualPTZ(configuracaoVisualPtzDTO);
		}

	}

	public void recreateCameraPipelineWithFlip(CameraDTO camera, String command) {
		// Base da pipeline
		String basePipeline = mapaBasePipeline.get(camera.getCodigo());

		// Substitui o valor de method pelo valor de command
		basePipeline = basePipeline.replaceAll("method=\\S+", "method=" + command);

		mapaBasePipeline.put(camera.getCodigo(), basePipeline);
		basePipeline = null;

		// Chama o método para recriar a pipeline com o novo valor
		recreateCameraPipeline(camera);

		memorizeButton.setIcon(ICON_FIXAR_OFF);

	}

	public void recreateCameraPipelineWithRotation(CameraDTO camera, String command) {
		// Base da pipeline
		String basePipeline = mapaBasePipeline.get(camera.getCodigo());

		// Substitui o valor de video-direction pelo valor de command
		basePipeline = basePipeline.replaceAll("video-direction=\\S+", "video-direction=" + command);

		mapaBasePipeline.put(camera.getCodigo(), basePipeline);
		basePipeline = null;

		// Chama o método para recriar a pipeline com o novo valor
		recreateCameraPipeline(camera);

		memorizeButton.setIcon(ICON_FIXAR_OFF);

	}

	public void recreateCameraPipelineResize(CameraDTO camera) {
		// Obtém as dimensões atuais da janela interna
		int imageWidth = mapaInternalFrame.get(camera.getCodigo()).getWidth();
		int imageHeight = mapaInternalFrame.get(camera.getCodigo()).getHeight();

		// Recupera a pipeline original
		String basePipeline = mapaBasePipeline.get(camera.getCodigo());

		// Atualiza os valores de largura e altura no trecho 'videoscale' existente
		basePipeline = basePipeline.replaceAll("width=\\d+", "width=" + imageWidth).replaceAll("height=\\d+",
				"height=" + imageHeight);

		// Atualiza a definição da pipeline
		mapaBasePipeline.put(camera.getCodigo(), basePipeline);
		basePipeline = null;

		// Recria a pipeline com os novos parâmetros
		recreateCameraPipeline(camera);

		// Atualiza o ícone do botão de memorizar
		memorizeButton.setIcon(ICON_FIXAR_OFF);
	}

	public void recreateCameraPipeline(CameraDTO camera) {

		try {

			if (mapaPipeline.containsKey(camera.getCodigo())) {
				Pipeline pipeline = mapaPipeline.get(camera.getCodigo());

				if (pipeline != null) {
					logger.info("Parando e liberando pipeline para a câmera {}", camera.getCodigo());

					// 🔹 Define o pipeline para NULL antes de liberar
					pipeline.setState(State.NULL);

					// 🔹 Aguarda a transição para NULL antes de liberar
					while (pipeline.getState() != State.NULL) {
						try {
							Thread.sleep(100); // Pequeno delay para garantir a transição
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}

					// 🔹 Agora pode liberar o recurso
					pipeline.dispose();
					pipeline = null;
					mapaPipeline.remove(camera.getCodigo());
				}

				mapaFps.remove(camera.getCodigo());
				mapaLatenciaPacote.remove(camera.getCodigo());

			}

			Bin bin = (Bin) Gst.parseLaunch(mapaBasePipeline.get(camera.getCodigo()));

			mapaVideoComponente.put(camera.getCodigo(), new GstVideoComponent());

			if (mapaPipeline.containsKey(camera.getCodigo())) {
				Pipeline oldPipeline = mapaPipeline.get(camera.getCodigo());
				if (oldPipeline.isPlaying()) {
					oldPipeline.setState(State.NULL); // 📌 Garante que o GStreamer pare corretamente
					oldPipeline.dispose(); // Libera memória e recursos do GStreamer
					mapaPipeline.remove(camera.getCodigo()); // Remove do mapa para evitar referências pendentes
				}
			}

			mapaPipeline.put(camera.getCodigo(), new Pipeline());
			mapaPipeline.get(camera.getCodigo()).addMany(bin, mapaVideoComponente.get(camera.getCodigo()).getElement());
			Element.linkMany(bin, mapaVideoComponente.get(camera.getCodigo()).getElement());

			Element videoSink = mapaVideoComponente.get(camera.getCodigo()).getElement();
			videoSink.set("sync", false);
			mapaPipeline.get(camera.getCodigo()).add(videoSink);
			mapaPipeline.get(camera.getCodigo()).getElementByName(camera.getCodigo()).link(videoSink);

			mapaPipeline.get(camera.getCodigo()).setState(State.PAUSED);
			mapaPipeline.get(camera.getCodigo()).play();

			Pad sinkPad = videoSink.getStaticPad("sink");
			if (sinkPad != null) {
				mapaPad.put(camera.getCodigo(), sinkPad);
			}

			// Reset FPS contagem
			mapaStartTime.put(camera.getCodigo(), -1L);
			mapaFrameCount.put(camera.getCodigo(), 0);

			// Defina um intervalo mínimo de tempo para atualização do FPS
			long fpsUpdateInterval = TimeUnit.MILLISECONDS.toNanos(1000); // Atualizar a cada 500ms
			AtomicLong lastFpsUpdateTime = new AtomicLong(System.nanoTime());

			// Adiciona um "probe" no Pad para calcular e monitorar o FPS
			// Adiciona um "probe" no Pad para calcular FPS e latência entre pacotes
			mapaPad.get(camera.getCodigo()).addProbe(PadProbeType.BUFFER, (pad, info) -> {
				Buffer buffer = info.getBuffer();
				if (buffer != null) {
					long bufferTimestamp = buffer.getPresentationTimestamp();

					// Inicializa timestamps e contagem de frames se for o primeiro frame
					mapaStartTime.putIfAbsent(camera.getCodigo(), bufferTimestamp);
					mapaFrameCount.merge(camera.getCodigo(), 1, Integer::sum);

					// Tempo decorrido desde o primeiro frame
					long elapsedTime = bufferTimestamp - mapaStartTime.get(camera.getCodigo());
					double seconds = elapsedTime / (double) TimeUnit.SECONDS.toNanos(1);

					// 🔹 Medir latência entre pacotes
					long now = System.nanoTime();
					long lastTime = mapaLastPacketTime.getOrDefault(camera.getCodigo(), now);
					long packetLatency = (now - lastTime) / 1_000_000; // Convertendo para ms
					mapaLastPacketTime.put(camera.getCodigo(), now); // Atualiza o tempo do último pacote

					// 🔹 Armazena todas as latências para cálculo da média
					mapaLatenciasPacote.computeIfAbsent(camera.getCodigo(), k -> new ArrayList<>()).add(packetLatency);

					// Se o tempo decorrido for suficiente, calcula o FPS e a latência média
					if (now - lastFpsUpdateTime.get() >= fpsUpdateInterval && seconds >= 1.0) {
						double fps = mapaFrameCount.get(camera.getCodigo()) / seconds;

						// 🔹 Calcula a média da latência entre pacotes
						List<Long> latencias = mapaLatenciasPacote.get(camera.getCodigo());
						double avgPacketLatency = latencias.stream().mapToLong(Long::longValue).average().orElse(0);

						// 🔹 Armazena FPS e latência média entre pacotes
						mapaFps.put(camera.getCodigo(), fps);
						mapaLatenciaPacote.put(camera.getCodigo(), (long) avgPacketLatency);

						// Atualiza o overlay no Swing
						SwingUtilities.invokeLater(() -> {
							try {
								String overlayText = String.format("%s - %s - FPS: %d", camera.getCodigo(),
										camera.getIp(), (int) Math.round(fps));

								VideoOverlayManager.updateOverlayText(camera, overlayText);
							} catch (NullPointerException npe) {
								npe.printStackTrace();
							}
						});

						// Reset das contagens para iniciar uma nova medição
						mapaFrameCount.put(camera.getCodigo(), 0);
						mapaStartTime.put(camera.getCodigo(), bufferTimestamp);
						lastFpsUpdateTime.set(now);

						// 🔹 Limpa a lista de latências após o cálculo
						latencias.clear();
					}
				}
				return PadProbeReturn.OK;
			});

			// recreateCameraPipeline
			// videoPanel[index] = new
			// VideoPanelWithFPS(videoComponent,camera.getCodigo().getCodigo().concat("
			// ").concat(camera.getCodigo().getIp()));

			// videoPanel[index] = new JPanel();
			// videoPanel[index].setLayout(new BoxLayout(videoPanel[index],
			// BoxLayout.Y_AXIS));

			// videoComponent.setAlignmentX(JPanel.CENTER_ALIGNMENT);
			// videoComponent.setAlignmentY(JPanel.CENTER_ALIGNMENT);

			// videoPanel[index].add(videoComponent);

			// Atualizando a UI
			if (mapaPipeline.get(camera.getCodigo()).getState() == State.PLAYING) {
				logger.info(camera.getCodigo() + " Conectou...");
				AlertDisplay.resetTimer(camera.getCodigo());

				SwingUtilities.invokeLater(() -> {

					mapaInternalFrame.get(camera.getCodigo()).getContentPane().removeAll();

					VideoOverlayManager.addOverlay(camera, mapaInternalFrame.get(camera.getCodigo()),
							mapaVideoComponente.get(camera.getCodigo()),
							String.format("%s - %s - FPS: 0", camera.getCodigo(), camera.getIp()));

					mapaInternalFrame.get(camera.getCodigo()).getContentPane().revalidate();
					mapaInternalFrame.get(camera.getCodigo()).getContentPane().repaint();

				});

				if (mapaReconnectTimers.containsKey(camera.getCodigo())) {
					Timer timer = mapaReconnectTimers.get(camera.getCodigo());

					if (timer != null) {
						timer.setRepeats(false);
						timer.stop();
						mapaReconnectTimers.put(camera.getCodigo(), timer);

					}
				}

				String status = this.getStatusPorCamera(mapaPipeline.get(camera.getCodigo()), camera.getCodigo());

				if (camera.getPTZ() == 1 && status.equals("01")) {

					try {
						mapaOnvifDevice.put(camera.getCodigo(),
								new OnvifDevice(camera.getIp(), camera.getUsuario(), camera.getSenha()));
						String profileName = mapaOnvifDevice.get(camera.getCodigo()).getDevices().getProfiles().get(0)
								.getName();
						logger.info("Conexão bem-sucedida com a câmera ONVIF: " + camera.getCodigo() + ", Perfil: "
								+ profileName);

						PTZOverlayPanel ptzPanel = new PTZOverlayPanel(mapaOnvifDevice.get(camera.getCodigo()), this,
								camera);
						mapaPTZOverlayPanel.put(camera.getCodigo(), ptzPanel);

						// Obtém o JInternalFrame correspondente
						JInternalFrame frame = mapaInternalFrame.get(camera.getCodigo());

						if (frame != null) {
							frame.setGlassPane(ptzPanel); // Adiciona o painel PTZ
							ptzPanel.setVisible(false); // Inicialmente invisível

							// Adiciona eventos de mouse para mostrar/ocultar o painel de PTZ
							frame.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									ptzPanel.setVisible(true);
								}

								@Override
								public void mouseExited(MouseEvent e) {
									ptzPanel.setVisible(false);
								}
							});
						}

					} catch (IndexOutOfBoundsException e) {
						logger.error("Erro de índice ao acessar o perfil da câmera: " + camera.getCodigo()
								+ ". Verifique se a câmera possui perfis ONVIF configurados. Detalhes: " + e);
					} catch (ConnectException e) {
						logger.error("Erro de conexão ao acessar a câmera ONVIF com IP: " + camera.getIp()
								+ ". Verifique a conectividade de rede e as configurações da câmera. Detalhes: " + e);
					} catch (SOAPException e) {
						logger.error("Erro de comunicação SOAP ao acessar a câmera ONVIF com IP: " + camera.getIp()
								+ ". Verifique as credenciais ou a compatibilidade ONVIF. Detalhes: " + e);
					} catch (Exception e) {
						// Captura qualquer outra exceção não prevista
						logger.error(
								"Erro inesperado ao acessar a câmera ONVIF " + camera.getCodigo() + ". Detalhes: " + e,
								e);
					}
				} else {

					try {
						// Passando a instância correta do Viewer (this) e o index da câmera

						// Verifica se o RotationOverlayPanel já existe, se não, cria um novo
						mapaRotationOverlayPanel.computeIfAbsent(camera.getCodigo(),
								key -> new RotationOverlayPanel(this, camera));

						// Obtém os objetos do mapa
						RotationOverlayPanel rotationPanel = mapaRotationOverlayPanel.get(camera.getCodigo());
						JInternalFrame frame = mapaInternalFrame.get(camera.getCodigo());

						if (frame != null && rotationPanel != null) {
							frame.setGlassPane(rotationPanel); // Adiciona o painel de rotação

							// Define que o painel de rotação começa invisível
							rotationPanel.setVisible(false);

							// Adiciona eventos de mouse para mostrar/ocultar o painel de rotação
							frame.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									rotationPanel.setVisible(true); // Mostra o painel quando o mouse entra
								}

								@Override
								public void mouseExited(MouseEvent e) {
									rotationPanel.setVisible(false); // Oculta o painel quando o mouse sai
								}
							});
						}

					} catch (IndexOutOfBoundsException e) {
						logger.error("Erro de índice ao acessar o perfil da câmera: " + camera.getCodigo()
								+ ". Verifique se a câmera possui perfis ONVIF configurados. Detalhes: " + e);
					} catch (Exception e) {
						logger.error(
								"Erro inesperado ao acessar a câmera ONVIF " + camera.getCodigo() + ". Detalhes: " + e,
								e);
					}
				}
			}

			reconnectToErrorBus(camera);

		} catch (Exception ex) {
			logger.error(camera.getCodigo() + " Na tentativa de conexão...", ex);
		}

	}

	private void disposeInternalFrames() {
	    if (mapaInternalFrame != null) {
	        for (JInternalFrame frame : mapaInternalFrame.values()) {
	            if (frame != null && frame.isDisplayable()) {
	                try {
	                    if (SwingUtilities.isEventDispatchThread()) {
	                        frame.dispose();
	                    } else {
	                        SwingUtilities.invokeAndWait(frame::dispose);
	                    }
	                } catch (Exception e) {
	                    logger.warn("Erro ao descartar JInternalFrame", e);
	                }
	            }
	        }
	        mapaInternalFrame.clear();
	    }
	}

	private void shutdownTimersAndThreads() {
		if (animationThread != null && animationThread.isAlive()) {
			animationThread.interrupt();
			animationThread = null;
		}
		if (mapaReconnectTimers != null && !mapaReconnectTimers.isEmpty()) {
			for (String codigo : mapaReconnectTimers.keySet()) {
				Timer timer = mapaReconnectTimers.get(codigo);
				if (timer != null) {
					timer.stop();
					mapaReconnectTimers.put(codigo, timer);
				}
			}
			mapaReconnectTimers.clear(); // Remove todos os timers do mapa
		}
		logger.info("Timers e threads encerrados.");
	}

	private void cleanupPipelines() {
	    if (mapaCameras != null && !mapaCameras.isEmpty()) {
	        for (String codigo : mapaCameras.keySet()) {
	            try {
	                // Remover referências secundárias aos pipelines
	                mapaPad.remove(codigo);
	                mapaVideoComponente.remove(codigo);
	                mapaBasePipeline.remove(codigo);
	                mapaStartTime.remove(codigo);
	                mapaFrameCount.remove(codigo);

	                logger.info("Referências secundárias ao pipeline removidas para a câmera: {}", codigo);

	                // Limpar o pipeline principal
	                Pipeline pipeline = mapaPipeline.get(codigo);
	                if (pipeline != null) {
	                    pipeline.setState(State.NULL); // define o pipeline para estado NULL (necessário para liberar recursos)
	                    pipeline.dispose();            // descarta efetivamente o pipeline
	                    logger.info("Pipeline descartado com sucesso para a câmera: {}", codigo);
	                }
	            } catch (Exception e) {
	                logger.error("Erro ao limpar recursos do pipeline para a câmera {}", codigo, e);
	            }
	        }
	        mapaPipeline.clear(); // remove referências ao pipeline do mapa principal após descarte
	    }
	}


	private void removerLinhasAbaixoDoCabecalhoPTZ() {

		scenarioFixed.forEach((name, fixed) ->
	    System.out.println("removerLinhasAbaixoDoCabecalhoPTZ: " + name + " = " + fixed)
	);
		
		Path path = pathvisualptzconf;

		String scenarioPrefix = "";

		for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
		    String name  = e.getKey();
		    Boolean fixed = e.getValue();

		    System.out.println("inicializaGridFree: " + name + " = " + fixed);

		    if (Boolean.TRUE.equals(fixed)) {
		        scenarioPrefix = name.toLowerCase(Locale.ROOT);
		         break; 
		    }
		}

		if (!scenarioPrefix.isEmpty()) {

			pathcenariovisualptzconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual_ptz.cfg");

			path = pathcenariovisualptzconf;
		}

		try {
			// Lê todas as linhas do arquivo
			List<String> lines = Files.readAllLines(path);

			// Verifica se o arquivo não está vazio
			if (!lines.isEmpty()) {
				// Cria uma lista contendo apenas a primeira linha (cabeçalho)
				List<String> linesFiltered = List.of(lines.get(0));

				// Escreve apenas o cabeçalho de volta no arquivo
				Files.write(path, linesFiltered);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void gravarArquivoConfiguracaoVisualPTZ(ConfiguracaoVisualPtzDTO configuracaoVisualPtzDTO) {

		Path path = pathvisualptzconf;

		String scenarioPrefix = "";

		for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
		    String name  = e.getKey();
		    Boolean fixed = e.getValue();

		    System.out.println("inicializaGridFree: " + name + " = " + fixed);

		    if (Boolean.TRUE.equals(fixed)) {
		        scenarioPrefix = name.toLowerCase(Locale.ROOT);
		         break; 
		    }
		}

		if (!scenarioPrefix.isEmpty()) {

			pathcenariovisualptzconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual_ptz.cfg");

			path = pathcenariovisualptzconf;
		}

		String line = configuracaoVisualPtzDTO.getCodigoPTZ() + "|" + configuracaoVisualPtzDTO.getEstado();

		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND,
				StandardOpenOption.CREATE)) {
			if (Files.size(path) == 0) {
				writer.write("codptz|estado");
				writer.newLine();
			}
			writer.write(line);
			writer.newLine();

		} catch (IOException e) {

		}
	}

	private void gravarArquivoConfiguracaoVisual(ConfiguracaoVisualDTO dto) {
	    try {
	        String novaLinha = String.join("|",
	                dto.getCodigoCamera(),
	                String.valueOf(dto.getTamanhoHorizontal()),
	                String.valueOf(dto.getTamanhoVertical()),
	                String.valueOf(dto.getPosicaoHorizontal()),
	                String.valueOf(dto.getPosicaoVertical()),
	                String.valueOf(dto.getRotate()),
	                String.valueOf(dto.getFlip())
	        );

	        String scenarioPrefix = "";

			for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
			    String name  = e.getKey();
			    Boolean fixed = e.getValue();

			    if (Boolean.TRUE.equals(fixed)) {
			        scenarioPrefix = name.toLowerCase(Locale.ROOT);
			         break; 
			    }
			}

	        Path path = scenarioPrefix.isEmpty() ?
	                pathvisualconf :
	                Paths.get(System.getProperty("user.dir"), "configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual.cfg");

	        List<String> linhas = Files.exists(path) ? Files.readAllLines(path) : new ArrayList<>();
	        linhas.removeIf(String::isBlank); // remove linhas vazias

	        if (linhas.isEmpty() || !linhas.get(0).startsWith("codcam|")) {
	            linhas.add(0, "codcam|tamh|tamv|posh|posv|rot|flip");
	        }

	        // Atualiza ou adiciona
	        boolean updated = false;
	        for (int i = 1; i < linhas.size(); i++) {
	            if (linhas.get(i).startsWith(dto.getCodigoCamera() + "|")) {
	                linhas.set(i, novaLinha);
	                updated = true;
	                break;
	            }
	        }

	        if (!updated) {
	            linhas.add(novaLinha);
	        }

	        // Opcional: ordenar as linhas pelo código da câmera
	        linhas = Stream.concat(Stream.of(linhas.get(0)), // mantém cabeçalho
	                        linhas.subList(1, linhas.size()).stream()
	                              .sorted(Comparator.comparing(s -> s.split("\\|")[0])))
	                .collect(Collectors.toList());

	        Files.write(path, linhas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
	        memorizeButton.setIcon(ICON_FIXAR_ON);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public List<ConfiguracaoVisualDTO> loadConfigurationsFromVisual() {

		createFileIfNotExists();

		try {
			List<String> lines = Files.readAllLines(pathvisualconf);
			return lines.stream().filter(line -> !line.isEmpty() && !line.startsWith("codcam"))
					.map(this::parseLineToDTO).filter(dto -> dto != null) // Filtra nulos gerados por parsing com erro
					.toList();
		} catch (IOException e) {
			logger.error("Erro ao ler o arquivo de configuração visual", e);
			return Collections.emptyList();
		}
	}

	public List<ConfiguracaoVisualDTO> loadVisualScenarioConfigurations(String scenarioPrefix) {
				
		pathscenariovisualconf = Paths.get(System.getProperty("user.dir"),
				"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual.cfg");

		createScenarioFileIfNotExists(scenarioPrefix);

		try {
			List<String> lines = Files.readAllLines(pathscenariovisualconf);
			return lines.stream().filter(line -> !line.isEmpty() && !line.startsWith("codcam"))
					.map(this::parseLineToDTO).filter(dto -> dto != null) // Filtra nulos gerados por parsing com erro
					.toList();
		} catch (IOException e) {
			logger.error("Erro ao ler o arquivo de configuração visual", e);
			return Collections.emptyList();
		}
	}

	public List<ConfiguracaoVisualPtzDTO> loadConfigurationsFromVisualPTZ() {
		
		createFileConfPtzIfNotExists();

		Path path = pathvisualptzconf;

		String scenarioPrefix = "";

		for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
		    String name  = e.getKey();
		    Boolean fixed = e.getValue();

		    if (Boolean.TRUE.equals(fixed)) {
		        scenarioPrefix = name.toLowerCase(Locale.ROOT);
		         break; 
		    }
		}

		if (!scenarioPrefix.isEmpty()) {

			pathcenariovisualptzconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual_ptz.cfg");

			path = pathcenariovisualptzconf;
		}

		try {
			List<String> lines = Files.readAllLines(path);
			return lines.stream().filter(line -> !line.isEmpty() && !line.startsWith("codptz"))
					.map(this::parsePtzLineToDTO).filter(dto -> dto != null) // Filtra nulos gerados por parsing com
																				// erro
					.toList();
		} catch (IOException e) {
			logger.error("Erro ao ler o arquivo de configuração visual da ptz", e);
			return Collections.emptyList();
		}
	}

	private void createScenarioFileIfNotExists(String scenarioPrefix) {
		try {

			pathscenariovisualconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual.cfg");

			Files.createDirectories(pathscenariovisualconf.getParent());
			if (!Files.exists(pathscenariovisualconf)) {
				Files.createFile(pathscenariovisualconf);
			}
		} catch (IOException e) {
			logger.error("Erro ao criar o arquivo de configuração visual do cenário", e);
		}
	}

	private void createFileIfNotExists() {
		try {
			Files.createDirectories(pathvisualconf.getParent());
			if (!Files.exists(pathvisualconf)) {
				Files.createFile(pathvisualconf);
			}
		} catch (IOException e) {
			logger.error("Erro ao criar o arquivo de configuração visual", e);
		}
	}

	private void createFileConfPtzIfNotExists() {
		
		Path path = pathvisualptzconf;

		String scenarioPrefix = "";

		for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
		    String name  = e.getKey();
		    Boolean fixed = e.getValue();

		    if (Boolean.TRUE.equals(fixed)) {
		        scenarioPrefix = name.toLowerCase(Locale.ROOT);
		         break; 
		    }
		}

		if (!scenarioPrefix.isEmpty()) {

			pathcenariovisualptzconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual_ptz.cfg");

			path = pathcenariovisualptzconf;
		}

		try {
			Files.createDirectories(path.getParent());
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
		} catch (IOException e) {
			logger.error("Erro ao criar o arquivo de configuração visual", e);
		}
	}

	private ConfiguracaoVisualDTO parseLineToDTO(String line) {
		try {
			String[] fields = line.split("\\|");
			return new ConfiguracaoVisualDTO(fields[0], Integer.parseInt(fields[1]), Integer.parseInt(fields[2]),
					Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5], fields[6]);
		} catch (Exception e) {
			logger.error("Erro ao processar a linha para DTO: {}", line, e);

			// Tratativa para recriar o arquivo de configuração
			try {
				
				logger.warn("O arquivo de configuração de layout está inválido. Tentando recriar o arquivo...");

				String scenarioPrefix = "";

				for (Map.Entry<String, Boolean> e1 : scenarioFixed.entrySet()) {
				    String name  = e1.getKey();
				    Boolean fixed = e1.getValue();

				    if (Boolean.TRUE.equals(fixed)) {
				        scenarioPrefix = name.toLowerCase(Locale.ROOT);
				         break; 
				    }
				}

				if (!scenarioPrefix.isEmpty()) {

					pathscenariovisualconf = Paths.get(System.getProperty("user.dir"),
							"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual.cfg");

					Files.deleteIfExists(pathscenariovisualconf);
					Files.createDirectories(pathscenariovisualconf.getParent());
					Files.createFile(pathscenariovisualconf);

				} else {

					Files.deleteIfExists(pathvisualconf);
					Files.createDirectories(pathvisualconf.getParent());
					Files.createFile(pathvisualconf);

				}

				JOptionPane.showMessageDialog(null,
						"O arquivo de configuração de layout está incompatível com a versão atual ou corrompido.\n"
								+ "Ele foi recriado automaticamente. Por favor, configure a disposição das câmeras e tente novamente.",
						"Erro: Configuração Inválida", JOptionPane.ERROR_MESSAGE);

			} catch (IOException e1) {
				logger.error("Erro ao recriar o arquivo de configuração de layout", e1);

				// Mensagem para o usuário em caso de falha ao recriar o arquivo
				JOptionPane.showMessageDialog(null,
						"Erro ao recriar o arquivo de configuração de layout. Verifique as permissões do sistema ou entre em contato com o suporte técnico.",
						"Erro Crítico", JOptionPane.ERROR_MESSAGE);
			}

			return null; // Retorna nulo para indicar falha no processamento da linha
		}
	}

	// TODO
	private ConfiguracaoVisualPtzDTO parsePtzLineToDTO(String line) {
		
		Path path = pathvisualptzconf;

		String scenarioPrefix = "";

		for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
		    String name  = e.getKey();
		    Boolean fixed = e.getValue();

		    if (Boolean.TRUE.equals(fixed)) {
		        scenarioPrefix = name.toLowerCase(Locale.ROOT);
		         break; 
		    }
		}

		if (!scenarioPrefix.isEmpty()) {

			pathcenariovisualptzconf = Paths.get(System.getProperty("user.dir"),
					"configuracao/cenarios/" + usuario + "_" + scenarioPrefix + "_visual_ptz.cfg");

			path = pathcenariovisualptzconf;
		}

		try {
			String[] fields = line.split("\\|");
			return new ConfiguracaoVisualPtzDTO(fields[0], Integer.parseInt(fields[1]));
		} catch (Exception e) {
			logger.error("Erro ao processar a linha para PTZ DTO: {}", line, e);

			// Tratativa para recriar o arquivo de configuração
			try {
				logger.warn("O arquivo de configuração de layout da ptz está inválido. Tentando recriar o arquivo...");
				Files.deleteIfExists(path);
				Files.createDirectories(path.getParent());
				Files.createFile(path);

				JOptionPane.showMessageDialog(null,
						"O arquivo de configuração de layout está incompatível com a versão atual ou corrompido.\n"
								+ "Ele foi recriado automaticamente. Por favor, configure a disposição das câmeras e tente novamente.",
						"Erro: Configuração Inválida", JOptionPane.ERROR_MESSAGE);

			} catch (IOException e1) {
				logger.error("Erro ao recriar o arquivo de configuração de layout da PTZ.", e1);

				// Mensagem para o usuário em caso de falha ao recriar o arquivo
				JOptionPane.showMessageDialog(null,
						"Erro ao recriar o arquivo de configuração de layout da PTZ. Verifique as permissões do sistema ou entre em contato com o suporte técnico.",
						"Erro Crítico", JOptionPane.ERROR_MESSAGE);
			}

			return null; // Retorna nulo para indicar falha no processamento da linha
		}
	}

	private int getTaskBarHeight() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize().height
				- java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}

	public Map<String, Integer> verificaFpsPorCamera() {
		Map<String, Integer> fpsPorCamera = new LinkedHashMap<>();

		// Garante 16 câmeras fixas: CAM-1 a CAM-16
		for (int i = 1; i <= 16; i++) {
			String cameraCode = "CAM-" + i;
			int fpsInt = 0; // valor padrão

			if (mapaCameras != null && mapaFps != null && mapaCameras.containsKey(cameraCode)) {
				Double fps = mapaFps.get(cameraCode);

				if (fps != null) {
					try {
						fpsInt = (int) Math.min(255, Math.round(fps));
					} catch (NumberFormatException e) {
						logger.warn("FPS inválido para {}: {}", cameraCode, e.getMessage());
					}
				}
			}

			fpsPorCamera.put(cameraCode, fpsInt);
		}

		return fpsPorCamera;
	}

	public byte[] verificaStatusCameras() {
	    final int CAMERAS_MAX = 16;
	    final int BITS_POR_CAMERA = 2;
	    final int BYTES = (CAMERAS_MAX * BITS_POR_CAMERA + 7) / 8; // 4
	    byte[] data = new byte[BYTES];

	    // 1) Pré-preenche: todas como NAO_CONFIGURADA (2 bits '10' => 0xAA por byte)
	    Arrays.fill(data, (byte) 0xAA);

	    // 2) Garante lista carregada
	    if (listaParaStatusCamera == null || listaParaStatusCamera.isEmpty()) {
	        listaParaStatusCamera = CameraConfigurationManager.get();
	        if (listaParaStatusCamera == null || listaParaStatusCamera.isEmpty()) {
	            return data; // nada a atualizar; todas permanecem '10'
	        }
	    }

	    // 3) Mapa por código para lookup rápido
	    Map<String, CameraDTO> mapaCameras = mapearCamerasPorCodigo(listaParaStatusCamera);

	    // 4) Atualiza apenas as câmeras configuradas
	    for (String cameraCode : mapaCameras.keySet()) {
	        int idx = parseIndice(cameraCode); // CAM-1..CAM-16 => 1..16
	        if (idx < 1 || idx > CAMERAS_MAX) {
	            logger.warn("Código de câmera fora do intervalo esperado: {}", cameraCode);
	            continue;
	        }

	        Pipeline pipeline = mapaPipeline.get(cameraCode);

	        // Obtém status real ("00","01","11" ou "10" se necessário)
	        String status = getStatusPorCamera(pipeline, cameraCode);
	        int statusValue = switch (status) {
	            case "01" -> 0b01; // ATIVA
	            case "00" -> 0b00; // CONFIGURADA_INATIVA
	            case "11" -> 0b11; // ERRO
	            default    -> 0b10; // NAO_CONFIGURADA (fallback)
	        };

	        // 5) Sobrescreve os 2 bits correspondentes
	        int bitPosition = (idx - 1) * BITS_POR_CAMERA; // 0,2,4,...,30
	        int byteIndex   = bitPosition / 8;             // 0..3
	        int bitOffset   = bitPosition % 8;             // 0,2,4,6
	        int shift       = 6 - bitOffset;               // 6,4,2,0

	        // limpa os 2 bits daquela posição e aplica o novo valor
	        int clearMask = ~(0b11 << shift) & 0xFF;
	        data[byteIndex] = (byte) ((data[byteIndex] & clearMask) | ((statusValue & 0b11) << shift));

	        logger.debug("Câmera {} - Status {} aplicado em byte {}, shift {}", cameraCode, status, byteIndex, shift);
	    }

	    return data;
	}

	private int parseIndice(String cameraCode) {
	    // Aceita "CAM-1" .. "CAM-16" (robusto a espaços)
	    if (cameraCode == null) return -1;
	    try {
	        String[] p = cameraCode.trim().split("-");
	        return (p.length == 2) ? Integer.parseInt(p[1]) : -1;
	    } catch (NumberFormatException e) {
	        return -1;
	    }
	}


	// Estados auto-documentados
	private enum CameraEstado {
	    CONFIGURADA_INATIVA("00", 0b00),
	    ATIVA("01", 0b01),
	    NAO_CONFIGURADA("10", 0b10),
	    ERRO("11", 0b11);

	    private final String bits;
	    private final int valor;
	    CameraEstado(String bits, int valor) { this.bits = bits; this.valor = valor; }
	    String bits() { return bits; }
	    int valor() { return valor; }
	}

	// Traduz sua lógica atual de status (inclui null-safe)
	private CameraEstado obterEstado(Pipeline pipeline, String cameraCode) {
	    String s = getStatusPorCamera(pipeline, cameraCode); // "00","01","10","11"
	    if (s == null) return CameraEstado.ERRO;
	    return switch (s) {
	        case "01" -> CameraEstado.ATIVA;
	        case "00" -> CameraEstado.CONFIGURADA_INATIVA;
	        case "11" -> CameraEstado.ERRO;
	        default    -> CameraEstado.NAO_CONFIGURADA;
	    };
	}


	public void imprimirStatusDasCameras(byte[] data) {
		for (int i = 0; i < 16; i++) {
			int bitPosition = i * 2;
			int byteIndex = bitPosition / 8;
			int bitOffset = bitPosition % 8;

			int shift = 6 - bitOffset;
			int statusBits = (data[byteIndex] >> shift) & 0b11;

			String statusTexto = switch (statusBits) {
			case 0b00 -> "CONFIGURADA";
			case 0b01 -> "ATIVA";
			case 0b10 -> "NÃO CONFIGURADA";
			case 0b11 -> "ERRO";
			default -> "DESCONHECIDO";
			};

			System.out.printf("CAM-%d: %s (bits: %02d)\n", i + 1, statusTexto, statusBits);
		}
	}

	private String getStatusPorCamera(Pipeline pipeline, String cameraCode) {

		if (pipeline == null) {
			return "00"; // Retorna "00" se a câmera não estiver mapeada
		}

		try {
			State state = pipeline.getState();
			return (state == State.PLAYING) ? "01" : "11";
		} catch (IllegalStateException e) {
			logger.error("Erro ao acessar o estado da câmera {}: {}", cameraCode, e.getMessage());
			return "11"; // Retorna "11" se houver erro
		}
	}

	private void addItem(JMenu menu, String label, Font font, Runnable action) {

		JMenuItem menuItem = new JMenuItem(label);
		menuItem.setFont(font);
		menuItem.addActionListener(e -> {
			menuInternalFrame.getContentPane().removeAll();
			action.run();
			menuInternalFrame.pack();

			// Define tamanho específico para "Linhas de Referência"
			if (label.equals("Linhas de Referência")) {
				menuInternalFrame.setSize(1295, 810);
			} else {
				menuInternalFrame.setSize(600, 540);
			}

			menuInternalFrame.setVisible(true);
			menuInternalFrame.revalidate();
			menuInternalFrame.repaint();
		});
		menu.add(menuItem);
	}

	public JMenuBar createHelpBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu configuracaoMenu = new JMenu("Ajuda");
		Font menuFont = new Font("Roboto", Font.PLAIN, 12);
		configuracaoMenu.setFont(menuFont);

		addItem(configuracaoMenu, "Sobre", menuFont, () -> menuInternalFrame.add(new SobreConfigurationPanel()));

		menuBar.add(configuracaoMenu);
		return menuBar;
	}

	public JMenuBar autorizacaoMenuBar() {

	    JMenuBar menuBar = new JMenuBar();
	    JPanel senhaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
	    senhaPanel.setOpaque(false);

	    // Campo Autorização
	    JLabel labelSenha = new JLabel("Autorização:");
	    labelSenha.setFont(new Font("Roboto", Font.PLAIN, 12));

	    JPasswordField campoSenha = new JPasswordField(10);
	    campoSenha.setForeground(Color.WHITE);
	    campoSenha.setMaximumSize(new Dimension(100, 24));

	    campoSenha.addActionListener((ActionEvent ev) -> {
	        String senha = new String(campoSenha.getPassword()).trim();

	        String chaveAutorizacao = null;

	        AutorizacaoDTO autorizacaoDTO = AutorizacaoConfigurationManager.get();

	        if (autorizacaoDTO == null) {
	        	
	        	autorizacaoDTO = new AutorizacaoDTO();
				autorizacaoDTO.setUsuario(usuario);
				autorizacaoDTO.setAutorizacao("S4etech@2025");
	        
	        }	
	        
	        chaveAutorizacao = autorizacaoDTO.getAutorizacao();
	        
	        if (chaveAutorizacao.equals(senha)) {
	            this.setAutorizado(true);
	            campoSenha.setEditable(false);
	            campoSenha.setForeground(Color.GREEN);

	            // Oculta os elementos após 1 segundo
	            Timer timer = new Timer(1000, e -> {
	                labelSenha.setVisible(false);
	                campoSenha.setVisible(false);
	                senhaPanel.setVisible(false);
	                menuBar.revalidate();
	                menuBar.repaint();
	            });

	            timer.setRepeats(false);
	            timer.start();

	            // Atualiza o menu agora que está autorizado
	            configuracaoMenu.removeAll();
	            List<MenuItem> novosItens = getMenuItemsAutorizados();

	            for (MenuItem item : novosItens) {
	                addItem(configuracaoMenu, item.getName(), item.getFont(), item.getAction());
	            }

	            menuBar.revalidate();
	            menuBar.repaint();

	        } else {
	            this.setAutorizado(false);
	            campoSenha.setText(senha);
	            campoSenha.setForeground(Color.RED);

	            Timer timer = new Timer(1500, evt -> {
	                campoSenha.setText("");
	                campoSenha.setForeground(Color.WHITE);
	            });

	            timer.setRepeats(false);
	            timer.start();
	        }
	    });

	    // Ordem: Usuário -> campo, depois Autorização -> campo
	    senhaPanel.add(labelSenha);
	    senhaPanel.add(campoSenha);
	    menuBar.add(senhaPanel);

	    return menuBar;
	}


	private List<MenuItem> getMenuItemsAutorizados() {
		List<MenuItem> list = new ArrayList<>();

		list.add(new MenuItem("Autorização", menuFont,
				() -> menuInternalFrame.add(new AutorizacaoConfigurationPanel(usuario, menuInternalFrame))));

		list.add(new MenuItem("Camera", menuFont,
				() -> menuInternalFrame.add(new CameraConfigurationPanel(usuario, menuInternalFrame))));

		list.add(new MenuItem("Cenário", menuFont,
				() -> menuInternalFrame.add(new CenarioConfigurationPanel(usuario, menuInternalFrame))));

		list.add(new MenuItem("Gravação", menuFont,
				() -> menuInternalFrame.add(new GravacaoConfigurationPanel(menuInternalFrame))));

		list.add(new MenuItem("Linhas de Referência", menuFont, () -> {
			LinhasDeReferenciaConfiguration frame = new LinhasDeReferenciaConfiguration();
			menuInternalFrame.add(frame);
			frame.setVisible(true);
			try {
				frame.setSelected(true);
			} catch (java.beans.PropertyVetoException e) {
				e.printStackTrace();
			}
		}));

		list.add(new MenuItem("Performance", menuFont,
				() -> menuInternalFrame.add(new PerformanceConfigurationPanel(menuInternalFrame))));

		list.add(new MenuItem("UDP Connection", menuFont,
				() -> menuInternalFrame.add(new UDPConfigurationPanel(menuInternalFrame, this))));

		list.add(new MenuItem("VNC", menuFont,
				() -> menuInternalFrame.add(new VNCConfigurationPanel(menuInternalFrame))));

		list.add(new MenuItem("Web Browser", menuFont,
				() -> menuInternalFrame.add(new WebBrowserConfigurationPanel(menuInternalFrame))));

		return list;
	}

	public JMenuBar createMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		configuracaoMenu = new JMenu("Configuração");
		menuFont = new Font("Roboto", Font.PLAIN, 12);
		configuracaoMenu.setFont(menuFont);

		// Criar uma lista de itens do menu
		List<MenuItem> menuItems = new ArrayList<>();

		if (isAutorizado()) {

			menuItems = getMenuItemsAutorizados();

		}

		// Ordenar os itens do menu pelo nome
		menuItems.sort(Comparator.comparing(MenuItem::getName));

		// Reconstruir o menu de configurações com acesso total
		configuracaoMenu.removeAll(); // Limpa os itens anteriores

		for (MenuItem item : menuItems) {
			addItem(configuracaoMenu, item.getName(), item.getFont(), item.getAction());
		}

		menuBar.revalidate();
		menuBar.repaint();

		menuBar.add(configuracaoMenu);
		return menuBar;
	}

	private void enviaStatusCompletoCamerasUDP() {
		cameraStatusExecutor.submit(() -> {
			if (!runningUDP.get())
				return;

			try {
				if (udpSocket == null) {
					udpSocket = new DatagramSocket();
				}

				// Coleta os dados
				Map<String, Integer> status = verificaStatusPorCamera();
				Map<String, Integer> fps = verificaFpsPorCamera();
				Map<String, Long> latencia = calculaLatenciaPorCamera();

				for (String cam : status.keySet()) {
					int st = status.getOrDefault(cam, 0);
					if (st != 1) {
						fps.put(cam, 0);
						latencia.put(cam, 0L);
					}
				}

				JsonObject dados = new JsonObject();
				long timestamp = System.currentTimeMillis();
				dados.addProperty("tipo", 0);
				dados.addProperty("timestamp", timestamp);
				JsonObject cenarios = construirListaCenarios(scenarioFixed);
				dados.add("cenarios", cenarios);

				JsonObject statusObj = new JsonObject();
				status.forEach((k, v) -> statusObj.addProperty(k.replace("CAM-", "CAM_"), v));
				dados.add("status", statusObj);

				JsonObject fpsObj = new JsonObject();
				fps.forEach((k, v) -> fpsObj.addProperty(k.replace("CAM-", "CAM_"), v));
				dados.add("fps", fpsObj);

				JsonObject latenciaObj = new JsonObject();
				latencia.forEach((k, v) -> latenciaObj.addProperty(k.replace("CAM-", "CAM_"), v));
				dados.add("latencia", latenciaObj);

				// Etapa 1: gerar JSON puro (sem envelope ainda)
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				String jsonSemCrc = gson.toJson(dados);

				// Etapa 2: calcular o CRC32 do JSON dos dados
				CRC32 crc = new CRC32();
				crc.update(jsonSemCrc.getBytes(StandardCharsets.UTF_8));
				String crcHex = String.format("0x%08X", crc.getValue());

				// Etapa 3: montar o envelope com "dados" e "crc"
				JsonObject envelope = new JsonObject();
				envelope.add("dados", dados);
				envelope.addProperty("crc", crcHex);

				// Etapa 4: enviar
				String jsonFinal = gson.toJson(envelope);
				byte[] data = jsonFinal.getBytes(StandardCharsets.UTF_8);

				DatagramPacket packet = new DatagramPacket(data, data.length,
						InetAddress.getByName(this.udpDTO.getIpDestino()),
						Integer.parseInt(this.udpDTO.getPortaDestino()));

				udpSocket.send(packet);
				
				//logger.info("[UDP ENVIADO] Para: " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " Conteúdo: " + jsonFinal);
				
				acksPendentes.put(timestamp, timestamp);

			} catch (Exception e) {
				logger.error("Erro ao enviar status completo das câmeras via UDP", e);
			}
		});
	}

	/**
	 * Monta "cenarios" garantindo:
	 * - S0 = "allCams" SEMPRE
	 * - S1..S6 = ordem do LinkedHashMap (exceto a chave "allCams", se existir)
	 * - nomes vazios -> "vago"
	 * - ativo: 1 se marcado no mapa, ou 1 em allCams quando nenhum outro ativo
	 */
	private JsonObject construirListaCenarios(Map<String, Boolean> scenarioFixed) {
	    JsonObject cenarios = new JsonObject();

	    // 1) Descobre se há algum ativo no mapa (fora do allCams)
	    boolean anyActive = false;
	    for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
	        if (!"allCams".equalsIgnoreCase(e.getKey()) && Boolean.TRUE.equals(e.getValue())) {
	            anyActive = true;
	            break;
	        }
	    }

	    // 2) S0 = allCams
	    int ativo0 = 0;
	    if (Boolean.TRUE.equals(scenarioFixed.get("allCams")) || !anyActive) {
	        ativo0 = 1;
	    }
	    JsonObject idx0 = new JsonObject();
	    idx0.addProperty("nome", "allCams");
	    idx0.addProperty("ativo", ativo0);
	    cenarios.add("S0", idx0);

	    // 3) S1..S6 = itens do mapa (ordem de inserção), pulando allCams
	    int idx = 1;
	    for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
	        if (idx >= TOTAL_CENARIOS) break;
	        String nome = e.getKey();
	        if ("allCams".equalsIgnoreCase(nome)) continue; // já usamos no S0

	        if (nome == null || nome.isBlank()) nome = "vago";

	        JsonObject c = new JsonObject();
	        c.addProperty("nome", nome);
	        c.addProperty("ativo", Boolean.TRUE.equals(e.getValue()) ? 1 : 0);
	        cenarios.add("S" + idx, c);
	        idx++;
	    }

	    // 4) Completa até S6 com "vago"
	    while (idx < TOTAL_CENARIOS) {
	        JsonObject c = new JsonObject();
	        c.addProperty("nome", "vago");
	        c.addProperty("ativo", 0);
	        cenarios.add("S" + idx, c);
	        idx++;
	    }

	    return cenarios;
	}



	/** Compatibilidade: retorna o primeiro ativo (0..6). Se nenhum, retorna 0 (allCams). */
	private int obterIndiceCenarioAtivo0Based(Map<String, Boolean> scenarioFixed) {
	    if (Boolean.TRUE.equals(scenarioFixed.get("allCams"))) return 0;

	    int idx = 1;
	    for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
	        if ("allCams".equalsIgnoreCase(e.getKey())) continue;
	        if (Boolean.TRUE.equals(e.getValue())) return idx;
	        idx++;
	        if (idx >= TOTAL_CENARIOS) break;
	    }
	    return 0;
	}

	
	private void startCommunicationUDP() {

		logger.info("Iniciando comunicação UDP...");
		udpLabel.setText("UDP ON");
		udpButton.setIcon(ICON_PCAN_OPERACIONAL);

		runningUDP.set(true);

		// Thread para envio periódico de status
		udpStatusThread = new Thread(() -> {
			try {
				while (runningUDP.get()) {
					enviaStatusCompletoCamerasUDP();
					Thread.sleep(500);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				logger.error("Thread de status UDP interrompida.", e);
			} catch (Exception e) {
				logger.error("Erro na thread de status UDP.", e);
			}
		}, "UDP-StatusThread");
		udpStatusThread.setDaemon(true);
		udpStatusThread.start();

		// Thread para escutar mensagens UDP
		udpAlarmeThread = new Thread(() -> {
			DatagramSocket socket = null;
			try {
				InetAddress localAddress = InetAddress.getByName(this.udpDTO.getIpLocal());
				int portaLocal = Integer.parseInt(this.udpDTO.getPortaLocal());
				socket = new DatagramSocket(new InetSocketAddress(localAddress, portaLocal));
				udpSocket = socket;
				byte[] buffer = new byte[1024];
				while (runningUDP.get()) {
					DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
					socket.receive(pacote);

					String ipRemetente = pacote.getAddress().getHostAddress();

					if (!ipRemetente.equals(this.udpDTO.getIpDestino())) {
						logger.warn("Pacote UDP ignorado. IP não autorizado: " + ipRemetente);
						continue;
					}

					String mensagem = new String(pacote.getData(), 0, pacote.getLength()).trim();
					
					//logger.info("[UDP RECEBIDO] De: " + ipRemetente + ":" + pacote.getPort() + " Conteúdo: " + mensagem);
					
					handleUdpMessage(mensagem);
				}
			} catch (SocketException e) {
				if (runningUDP.get()) {
					logger.error("Erro de socket UDP.", e);
				} else {
					logger.info("Socket UDP fechado com sucesso.");
				}
			} catch (Exception e) {
				logger.error("Erro no recebimento de UDP.", e);
			} finally {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			}
		}, "UDP-AlarmeThread");
		udpAlarmeThread.setDaemon(true);
		udpAlarmeThread.start();

		ackMonitorThread = new Thread(() -> {
			int falhasConsecutivas = 0;
			final int limiteFalhas = 3;
			boolean udpStatusAtivo = true;

			while (runningUDP.get()) {
				try {
					long agora = System.currentTimeMillis();
					List<Long> expirados = new ArrayList<>();
					boolean houveTimeout = false;

					synchronized (acksPendentes) {
						for (Map.Entry<Long, Long> entry : acksPendentes.entrySet()) {
							long enviado = entry.getValue();
							if (agora - enviado > 1000) {
								expirados.add(entry.getKey());
								houveTimeout = true;
							}
						}

						expirados.forEach(acksPendentes::remove);
					}

					if (houveTimeout) {
						falhasConsecutivas++;
					} else if (!acksPendentes.isEmpty()) {
						falhasConsecutivas = 0;
					}

					if (falhasConsecutivas >= limiteFalhas && udpStatusAtivo) {
						udpStatusAtivo = false;
						SwingUtilities.invokeLater(() -> {
							udpLabel.setText("UDP OFF");
							udpButton.setIcon(ICON_PCAN_DESCONECTADA);
						});
					} else if (falhasConsecutivas == 0 && !udpStatusAtivo) {
						udpStatusAtivo = true;
						SwingUtilities.invokeLater(() -> {
							udpLabel.setText("UDP ON");
							udpButton.setIcon(ICON_PCAN_OPERACIONAL);
						});
					}

					Thread.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					logger.error("ACK monitor interrompido.", e);
				}
			}
		}, "ACK-Monitor");

		ackMonitorThread.setDaemon(true);
		ackMonitorThread.start();
	}

	private void stopCommunicationUDP() {
		logger.info("Parando comunicação UDP...");

		runningUDP.set(false);

		try {
			if (udpSocket != null && !udpSocket.isClosed()) {
				udpSocket.close();
				udpSocket = null; // evita reutilização
				logger.info("Socket UDP fechado com sucesso.");
			}
		} catch (Exception e) {
			logger.error("Erro ao fechar o socket UDP", e);
		}

		if (timerMonitoramento != null) {
			timerMonitoramento.cancel();
			timerMonitoramento = null;
		}

		try {
			if (udpStatusThread != null && udpStatusThread.isAlive()) {
				udpStatusThread.join(1000);
				udpStatusThread = null;
			}
			if (udpAlarmeThread != null && udpAlarmeThread.isAlive()) {
				udpAlarmeThread.join(1000);
				udpAlarmeThread = null;
			}
			if (ackMonitorThread != null && ackMonitorThread.isAlive()) {
				ackMonitorThread.join(1000);
				ackMonitorThread = null;
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.warn("Thread interrompida ao aguardar encerramento das threads UDP.", e);
		}
	}

	public void reloadUDPConfig() {
		logger.info("Recarregando configuração UDP...");

		UDPDTO novaConfig = UDPConfigurationManager.get();
		if (novaConfig != null) {
			// Para a comunicação atual
			stopCommunicationUDP();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Atualiza e reinicia
			this.udpDTO = novaConfig;
			startCommunicationUDP();

			logger.info("Configuração UDP recarregada com sucesso.");
		} else {
			logger.warn("Falha ao recarregar a configuração UDP. Configuração nula.");
		}
	}

	private Map<String, Long> calculaLatenciaPorCamera() {
		Map<String, Long> latenciaPorCamera = new LinkedHashMap<>();

		for (int i = 1; i <= 16; i++) {
			String cameraCode = "CAM-" + i;
			long latencia = 0L; // Valor padrão para câmera ausente

			if (mapaCameras != null && mapaLatenciaPacote != null && mapaCameras.containsKey(cameraCode)) {
				Long valor = mapaLatenciaPacote.get(cameraCode);
				if (valor != null) {
					latencia = valor;
				} else {
					latencia = 0L; // câmera existe, mas sem latência medida
				}
			}

			latenciaPorCamera.put(cameraCode, latencia);
		}

		return latenciaPorCamera;
	}

	private Map<String, Integer> verificaStatusPorCamera() {
		Map<String, Integer> statusPorCamera = new LinkedHashMap<>();

		byte[] statusCameras = verificaStatusCameras(); // Deve retornar exatamente 4 bytes (para 16 câmeras * 2 bits)
		if (statusCameras == null || statusCameras.length < 4)
			return statusPorCamera;

		for (int i = 0; i < 16; i++) {
			String cam = "CAM-" + (i + 1);

			int bitPosition = i * 2;
			int byteIndex = bitPosition / 8;
			int bitOffset = bitPosition % 8;

			if (byteIndex < statusCameras.length) {

				int shift = 6 - bitOffset;
				int raw = ((statusCameras[byteIndex] & 0xFF) >> shift) & 0b11;

				// Adiciona ao mapa usando o enum
				try {
					statusPorCamera.put(cam, StatusCamera.fromValor(raw).getValor());
				} catch (IllegalArgumentException e) {
					statusPorCamera.put(cam, StatusCamera.ERRO.getValor());
				}
			} else {
				statusPorCamera.put(cam, StatusCamera.ERRO.getValor());
			}
		}

		return statusPorCamera;
	}

	private int obterStatusCenario() {

	    int i = 1; // começa em 1 para já bater com sua regra
	    for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
	        if (Boolean.TRUE.equals(e.getValue())) {
	            return i; // achou, retorna 1,2,3...
	        }
	        i++;
	    }

	    return 0; // se não achou nenhum
	}

	private void handleUdpMessage(String mensagem) {
	    try {
	        JsonObject envelope = JsonParser.parseString(mensagem).getAsJsonObject();

	        JsonObject json = (envelope.has("dados") && envelope.has("crc"))
	                ? envelope.getAsJsonObject("dados")
	                : envelope;

	        if (!json.has("tipo")) {
	            logger.warn("Mensagem UDP sem tipo: {}", mensagem);
	            return;
	        }

	        int tipoEnvio = json.get("tipo").getAsInt();

	        switch (tipoEnvio) {
	            case 1: { // CENÁRIO
	                if (animationThread != null && animationThread.isAlive()) {
	                    logger.info("Mudança de cenário ignorada: o sistema ainda está processando a mudança anterior.");
	                    return;
	                }

	                if (!json.has("cenario") || !json.get("cenario").isJsonObject()) {
	                    logger.warn("Campo 'cenario' ausente ou inválido em tipo=1: {}", mensagem);
	                    return;
	                }

	                JsonObject cObj = json.getAsJsonObject("cenario");
	                int index = cObj.has("index") ? cObj.get("index").getAsInt() : 0;
	                String nome = cObj.has("nome") && !cObj.get("nome").isJsonNull()
	                        ? cObj.get("nome").getAsString().trim()
	                        : "";

	                if (index == 0) {
	                    desativarTodosBotoes();
	                    logger.info("Cenário recebido: allCams (index=0)");
	                    return;
	                }

	                int btnIdx = index - 1;
	                if (btnIdx < 0 || btnIdx >= buttons.size()) {
	                    logger.warn("Índice de cenário fora do intervalo: index={}, buttons.size()={}", index, buttons.size());
	                    return;
	                }

	                JButton botao = buttons.get(btnIdx);
	               
	                simulateButtonClick(botao, nome);
	                logger.info("Cenário aplicado: index={} nome='{}'", index, botao.getText());
	                break;
	            }

	            case 2: { // ALARME
	                if (json.has("alarme")) {
	                    int estado = json.get("alarme").getAsInt();
	                    if (estado == 1) {
	                        alarmHandler.playAlarm();
	                    } else if (estado == 0) {
	                        alarmHandler.stopAlarm();
	                    } else {
	                        logger.warn("Estado de alarme inválido: {}", estado);
	                    }
	                } else {
	                    logger.warn("Campo 'alarme' ausente em tipo=2: {}", mensagem);
	                }
	                break;
	            }

	            case 3: { // PTZ
	                if (json.has("ptz")) {
	                    JsonObject ptz = json.getAsJsonObject("ptz");
	                    int comando = ptz.get("comando").getAsInt();
	                    String camera = "CAM-" + ptz.get("camera").getAsInt();
	                    controlarPtzViaUdp(camera, comando);
	                } else {
	                    logger.warn("Campo 'ptz' ausente em tipo=3: {}", mensagem);
	                }
	                break;
	            }

	            case 0: { // ACK
	                if (json.has("timestamp")) {
	                    long timestamp = json.get("timestamp").getAsLong();
	                    acksPendentes.remove(timestamp);
	                    udpLabel.setText("UDP ON");
	                    udpButton.setIcon(ICON_PCAN_OPERACIONAL);
	                } else {
	                    logger.warn("Campo 'timestamp' ausente em tipo=0 (ACK)");
	                }
	                break;
	            }

	            default:
	                logger.warn("Tipo de envio desconhecido: {}", tipoEnvio);
	                break;
	        }

	    } catch (Exception e) {
	        logger.error("Erro ao interpretar JSON UDP: {}", mensagem, e);
	    }
	}



	private void controlarPtzViaUdp(String camera, int comandoPtz) {
		if (!mapaOnvifDevice.containsKey(camera)) {
			logger.error("Erro: A câmera {} não foi encontrada no mapaOnvifDevice.", camera);
			return;
		}

		var deviceWrapper = mapaOnvifDevice.get(camera);
		if (deviceWrapper == null || deviceWrapper.getPtz() == null) {
			logger.error("Erro: O dispositivo PTZ da câmera {} é nulo ou inválido.", camera);
			return;
		}

		PtzDevices ptzDevices = deviceWrapper.getPtz();
		List<Profile> profiles = deviceWrapper.getDevices().getProfiles();
		if (profiles == null || profiles.isEmpty()) {
			logger.error("Erro: Nenhum perfil encontrado para a câmera {}", camera);
			return;
		}

		String profileToken = profiles.get(0).getToken();
		if (profileToken == null || profileToken.isEmpty()) {
			logger.error("Erro: Token do perfil da câmera {} está vazio.", camera);
			return;
		}

		float pan = 0.0f;
		float tilt = 0.0f;
		float zoom = 0.0f;
		int unsignedValue = comandoPtz & 0xFF;

		if (unsignedValue == 0x00) {
			ptzDevices.stopMove(profileToken);
		} else {
			// Tilt
			if ((unsignedValue & 0x10) != 0 && (unsignedValue & 0x20) == 0) {
				tilt = +0.5f;
			}
			if ((unsignedValue & 0x20) != 0 && (unsignedValue & 0x10) == 0) {
				tilt = -0.5f;
			}

			// Pan
			if ((unsignedValue & 0x04) != 0 && (unsignedValue & 0x08) == 0) {
				pan = +0.5f;
			}
			if ((unsignedValue & 0x08) != 0 && (unsignedValue & 0x04) == 0) {
				pan = -0.5f;
			}

			// Zoom
			if ((unsignedValue & 0x02) != 0 && (unsignedValue & 0x01) == 0) {
				zoom = +0.5f;
			}
			if ((unsignedValue & 0x01) != 0 && (unsignedValue & 0x02) == 0) {
				zoom = -0.5f;
			}

			ptzDevices.continuousMove(profileToken, pan, tilt, zoom);
		}
	}

	private void desativarTodosBotoes() {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setIcon(ICON_CENARIO_OFF);
			labels.get(i).setForeground(Color.GRAY);
		}

		scenarioFixed.replaceAll((k, v) -> false);
		isGridFreeShow=false;
		this.toggleGridFree();
		// logger.info("Todos os botões desativados via comando UDP.");
	}

	public void shutdownCameraStatusExecutor() {
		runningCameraStatusExecutor = false;
		cameraStatusExecutor.shutdown();
		try {
			if (!cameraStatusExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
				cameraStatusExecutor.shutdownNow();
			}
		} catch (InterruptedException e) {
			cameraStatusExecutor.shutdownNow();
			Thread.currentThread().interrupt();
		}
		logger.info("Executor de status das câmeras finalizado.");
	}


	public void simulateButtonClick(JButton button, String name) {
	    if (button == null || name == null) return;
	        	
    	// Destrói pipelines das câmeras explicitamente
	    if (mapaPipeline != null) {
	        mapaPipeline.keySet().forEach(this::destroyPipeline);
	        mapaPipeline.clear();
	        logger.info("Todos os pipelines destruídos.");
	    }
	    
	    String selected = name.toLowerCase(Locale.ROOT);
	    for (Map.Entry<String, Boolean> e : scenarioFixed.entrySet()) {
	        e.setValue(e.getKey().equals(selected));
	    }
	    scenarioFixed.putIfAbsent(selected, true);
	    
	    CenarioFileManager manager = new CenarioFileManager(usuario);
	    if (!manager.cenarioExiste(name.toUpperCase())) return;

	    // reset visual de todos
	    for (int i = 0; i < buttons.size(); i++) {
	        buttons.get(i).setIcon(ICON_CENARIO_OFF);
	        labels.get(i).setForeground(Color.GRAY);
	    }

	    // define selecionado
	    selectedIndex = buttons.indexOf(button);
	    if (selectedIndex >= 0) {
	        buttons.get(selectedIndex).setIcon(ICON_CENARIO_ON);
	        labels.get(selectedIndex).setForeground(Color.WHITE);
	    }

	    // se toggleGridFree recria a UI, re-aplique seleção após reconstruir
	    isGridFreeShow = false;
	    toggleGridFree();

	    // DICA: se toggleGridFree recria os botões, guarde o 'name' selecionado
	    // e, ao final da reconstrução, chame algo como:
	    // reselectByName(name);
	}



	// Método para verificar se a câmera está ativa
	private boolean isCameraActive(CameraDTO camera) {
		// Verifica se o pipeline está no estado PLAYING
		if (mapaPipeline.get(camera.getCodigo()) != null
				&& mapaPipeline.get(camera.getCodigo()).getState() == State.PLAYING) {
			// Verifica se o frame está visível
			if (mapaInternalFrame.get(camera.getCodigo()) != null
					&& mapaInternalFrame.get(camera.getCodigo()).isVisible()) {
				return true; // Câmera está ativa e visível
			}
		}
		return false; // Caso contrário, a câmera não está ativa ou visível
	}

	public boolean isAutorizado() {
		return this.isAutorizado;
	}

	public void setAutorizado(boolean autorizado) {
		this.isAutorizado = autorizado;
	}

}
