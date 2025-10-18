package com.s4etech.ui.screens;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatClientProperties;
import com.s4e.CryptoUtils;
import com.s4e.SystemInfo;
import com.s4etech.dto.AutorizacaoDTO;
import com.s4etech.dto.LicencaDTO;
import com.s4etech.dto.LoginDTO;
import com.s4etech.util.MessageUtils;

import net.miginfocom.swing.MigLayout;

public class Login extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    private static final long serialVersionUID = 1L;
    private static final String LOGIN_FILE = "configuracao/usuario.cfg";
    private static final String LICENCA_FILE = "licenca/licenca.cfg";
    private List<LoginDTO> listaLogin = new ArrayList<>();
    private List<LicencaDTO> listaLicenca = new ArrayList<>();
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtNumeroSerie;
    private JTextField txtLicenca;
    private JButton cmdLogin;
    private JLabel lblPasswordConfirm;
    private JPasswordField txtPasswordConfirm;
    private JLabel lbDescription = new JLabel();
    private JFrame framePrincipal;
    private boolean isAuthenticate = false;
    String numeroSerie = null;
    private static final List<String> VALORES_INVALIDOS = Arrays.asList(
    	    "", "unknown", "none", "null", "system serial number", 
    	    "to be filled by o.e.m.", "default string", "0000000000000000"
    	);

    public Login(JFrame frame, boolean isAuthenticate) {
    	this.isAuthenticate = isAuthenticate;
        this.framePrincipal = frame;
        boolean isAutorizado = false;
        carregarArquivoLicenca();
        String licenca = null;

        

        try {
            
        	numeroSerie = null;

        	// Tenta BIOS
        	try {
        	    numeroSerie = SystemInfo.getBiosSerialNumber();
        	    if (isSerialValido(numeroSerie)) {
        	        logger.info("Número de série do BIOS recuperado com sucesso: {}", numeroSerie);
        	    } else {
        	        logger.warn("Número de série do BIOS inválido: {}. Tentando placa-mãe...", numeroSerie);
        	        numeroSerie = null;
        	    }
        	} catch (Exception e) {
        	    logger.warn("Erro ao recuperar número de série do BIOS. Tentando placa-mãe...", e);
        	}
        	
        	// Tenta placa-mãe se BIOS falhou ou inválido
        	if (numeroSerie == null) {
        	    try {
        	        numeroSerie = SystemInfo.getMotherboardSerialNumber();
        	        if (isSerialValido(numeroSerie)) {
        	            logger.info("Número de série da placa-mãe recuperado com sucesso: {}", numeroSerie);
        	        } else {
        	            logger.warn("Número de série da placa-mãe inválido: {}. Tentando UUID...", numeroSerie);
        	            numeroSerie = null;
        	        }
        	    } catch (Exception e) {
        	        logger.warn("Erro ao recuperar número de série da placa-mãe. Tentando UUID...", e);
        	        numeroSerie = null;
        	    }
        	}

    
        	
        	// Tenta UUID se os anteriores falharam
        	if (numeroSerie == null) {
        	    try {
        	        numeroSerie = SystemInfo.getUUIDSerialNumber();
        	        if (isSerialValido(numeroSerie)) {
        	            logger.info("Número de série UUID recuperado com sucesso: {}", numeroSerie);
        	        } else {
        	            logger.error("Número de série UUID também inválido: {}", numeroSerie);
        	            numeroSerie = null;
        	        }
        	    } catch (Exception e) {
        	        logger.error("Erro ao recuperar número de série UUID.", e);
        	        numeroSerie = null;
        	    }
        	}
        	
        } catch (Exception e) {
            logger.error("Falha ao recuperar número de série da máquina (BIOS e placa-mãe).", e);
            MessageUtils.updateDescriptionLabel(lbDescription, "Número de Série não localizado.", Color.RED);
        }
            
        try {
            licenca = SystemInfo.shortKey(numeroSerie);
        } catch (Exception e) {
            logger.error("Falha ao recuperar a chave da licença.", e);
            MessageUtils.updateDescriptionLabel(lbDescription, "Falha ao recuperar a licença.", Color.RED);
        }
                
        for (LicencaDTO licencaDTO : listaLicenca) {
            if (numeroSerie != null && numeroSerie.equals(licencaDTO.getNumeroSerie()) && licenca != null
                    && licenca.equals(licencaDTO.getCodigoLicenca())) {
                isAutorizado = true;
                logger.info("Licença autorizada para número de série: {}", numeroSerie);
                break;
            }
        }

        if (isAutorizado) {
            carregarArquivoLogin();
            logger.info("Inicializando login padrão.");
            initLogin();
        } else {
            initLicenca();
        }
    }
    
    private boolean isSerialValido(String serial) {
        if (serial == null) return false;

        String s = serial.trim().toLowerCase();

        if (VALORES_INVALIDOS.contains(s)) return false;

        // Só letras? Rejeita
        if (s.matches("^[a-zA-Z]+$")) return false;

        // Aceita se for só números com pelo menos 5 dígitos
        if (s.matches("^\\d{5,}$")) return true;

        // Aceita se for alfanumérico com pelo menos 5 caracteres (letras e/ou números)
        return s.matches("^[a-zA-Z0-9\\-]{5,}$");
    }
    
    private void initLicenca() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        txtNumeroSerie = new JTextField();
        
        try {

            txtNumeroSerie.setText(numeroSerie);

        } catch (Exception e) {
            logger.error("Erro ao recuperar número de série para exibição (BIOS, Placa-Mãe ou UUID).", e);
            JOptionPane.showMessageDialog(
                null,
                "Não foi possível obter o número de série.\n" +
                "Verifique as configurações da instalação.\n" +
                "O sistema será encerrado.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        txtNumeroSerie.setEditable(false);

        txtLicenca = new JTextField();
        EventQueue.invokeLater(() -> txtLicenca.requestFocus());

        JButton cmdLogin = new JButton("Validar");
        JLabel lbTitle = new JLabel("Validar Licença.");
    	if(numeroSerie!=null) {
      		 MessageUtils.updateDescriptionLabel(lbDescription, "Cadastre a licença recebida.", Color.WHITE);
      	}
       
        
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 35 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:darken(@background,3%)");

        cmdLogin.addActionListener(e -> {
            try {
            	
            
            	
                String licencaGerada = SystemInfo.shortKey(numeroSerie);
                
    
                
                if (licencaGerada.equals(txtLicenca.getText().trim())) {
                    gravarArquivoLicenca(new LicencaDTO(txtNumeroSerie.getText().trim(), txtLicenca.getText().trim()));
                    logger.info("Licença válida registrada com sucesso.");
                    SwingUtilities.getWindowAncestor(this).dispose();
                    com.s4etech.Main.iniciarInterfaceUsuario();
                } else {
                    logger.warn("Licença digitada incorreta.");
                    MessageUtils.updateDescriptionLabel(lbDescription, "Licença inválida.", Color.RED);
                }
            } catch (Exception ex) {
                logger.error("Erro ao validar licença.", ex);
                MessageUtils.updateDescriptionLabel(lbDescription, "Erro ao validar licença.", Color.RED);
            }
        });

        panel.add(lbTitle, "gapbottom 10, span, align center");
        panel.add(lbDescription, "gapbottom 20, span, align center");
        panel.add(new JLabel("Número de Série:"), "gapbottom 2");
        panel.add(txtNumeroSerie, "gapbottom 10");
        panel.add(new JLabel("Licença:"), "gapbottom 2");
        panel.add(txtLicenca, "gapbottom 8");
        panel.add(cmdLogin, "gaptop 10, sizegroup btn");

        add(panel);
    }

    private void initLogin() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtPasswordConfirm = new JPasswordField();
        cmdLogin = new JButton("Login");
        JLabel lbTitle = new JLabel("Bem-vindo(a) de volta!");
        MessageUtils.updateDescriptionLabel(lbDescription,"Por favor, faça login para acessar a aplicação.", Color.WHITE);
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 35 45", "fill,250:280"));

        panel.putClientProperty(FlatClientProperties.STYLE, "" + "arc:20;" + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Entre com seu usuário.");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Entre com sua senha.");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        cmdLogin.putClientProperty(FlatClientProperties.STYLE,
                "" + "[light]background:darken(@background,10%);" + "[dark]background:lighten(@background,10%);"
                        + "margin:4,6,4,6;" + "borderWidth:0;" + "focusWidth:0;" + "innerFocusWidth:0");

        txtUsername.addActionListener((e) -> txtPassword.requestFocusInWindow());
        
     // Adiciona ActionListener ao campo txtPassword para executar o login ao pressionar Enter
        txtPassword.addActionListener((e) -> cmdLogin.doClick());
        
        cmdLogin.addActionListener((e) -> {

        	String devUsername = System.getenv("DEV");
        	
        	if(devUsername!=null && !devUsername.isEmpty() || isAuthenticate) {
        		
        			if(isAuthenticate) devUsername = "s4eviewer";
        		
        		 	logger.info("Login automático em modo de desenvolvimento para o usuário: {}", devUsername);

        		 	try {
        		 		
        		 		AutorizacaoDTO autorizacaoDTO = new AutorizacaoDTO();
        		 		autorizacaoDTO.setAutorizacao("S4etech@2025");
        		 		autorizacaoDTO.setUsuario(devUsername);
        		 		
        		 	    Viewer gui = new Viewer(autorizacaoDTO);
        		 	    gui.inicializa();
        		 	} catch (UnsatisfiedLinkError ex) {
        		 	    // Captura especificamente erro de biblioteca nativa não encontrada
        		 	    logger.error("Erro ao carregar a biblioteca PCANBasic_JNI. ", ex);
       		 	  

        		 	    // Se quiser exibir um popup em vez de usar lbDescription:
        		 	     JOptionPane.showMessageDialog(
        		 	         this,
        		 	         "Ocorreu um erro sistêmico, verifique as configurações (biblioteca nativa não encontrada).",
        		 	         "Erro",
        		 	         JOptionPane.ERROR_MESSAGE
        		 	     );
        		 	    
        		 	} catch (Exception ex) {
        		 		
        		 		logger.error("Erro ao inicializar o Viewer.", ex);

        		 	    JOptionPane.showMessageDialog(
        		 	        this,
        		 	        "Ocorreu um erro sistêmico, verifique as configurações.",
        		 	        "Erro",
        		 	        JOptionPane.ERROR_MESSAGE
        		 	    );
        		 	}


        	        Window window = SwingUtilities.getWindowAncestor(this);
        	        if (window instanceof JFrame) {
        	            ((JFrame) window).dispose();
        	        }
        	        return;
        	}
        	
            boolean loginValido = listaLogin.stream()
                    .anyMatch(login -> txtUsername.getText().equalsIgnoreCase(login.getUsuario())
                            && txtPassword.getText().equalsIgnoreCase(login.getSenha()));

            if (loginValido) {
                logger.info("Login bem-sucedido para o usuário: {}", txtUsername.getText());

                AutorizacaoDTO autorizacaoDTO = new AutorizacaoDTO();
		 		autorizacaoDTO.setAutorizacao("S4etech@2025");
		 		autorizacaoDTO.setUsuario(txtUsername.getText());
                
                Viewer gui = new Viewer(autorizacaoDTO);
                gui.inicializa();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                Window window = SwingUtilities.getWindowAncestor(this);

                if (window instanceof JFrame) {
                    ((JFrame) window).dispose();
                }

            } else {
                logger.warn("Tentativa de login falhou para o usuário: {}", txtUsername.getText());

                boolean usuarioExiste = listaLogin.stream()
                        .anyMatch(login -> txtUsername.getText().equalsIgnoreCase(login.getUsuario()));

                String decrypt = CryptoUtils.decrypt(txtPassword.getText().trim());

                if (usuarioExiste || decrypt == null || !decrypt.contains("Tech@2023")) {
                    if (!txtPasswordConfirm.getText().trim().isEmpty()) {
                        if (txtPassword.getText().trim().equalsIgnoreCase(txtPasswordConfirm.getText().trim())) {

                            LoginDTO loginDTO = new LoginDTO(txtUsername.getText().trim(),
                                    txtPasswordConfirm.getText().trim());

                            gravarArquivoLogin(loginDTO);

                            MessageUtils.updateDescriptionLabel(lbDescription,"Senha atualizada com sucesso.", Color.WHITE);

                            txtUsername.setText("");
                            txtPassword.setText("");
                            txtPasswordConfirm.setText("");

                            panel.remove(lblPasswordConfirm);
                            panel.remove(txtPasswordConfirm);

                            listaLogin = new ArrayList<>();
                            carregarArquivoLogin();
                            return;

                        } else {

                        	MessageUtils.updateDescriptionLabel(lbDescription,"As senhas não correspondem.", Color.RED);
                            return;

                        }
                    }

                    MessageUtils.updateDescriptionLabel(lbDescription,"Dados inválidos ou senha incorreta.", Color.RED);

                } else {

                    String[] parts = decrypt.split("\\|");

                    if (txtUsername.getText().trim().equalsIgnoreCase(parts[0].trim())) {

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
                        LocalDateTime validade = LocalDateTime.parse(parts[2], formatter);
                        LocalDateTime agora = LocalDateTime.now();
                        if (validade.isAfter(agora)) {

                            addConfirmationFields(panel);

                        } else {

                        	MessageUtils.updateDescriptionLabel(lbDescription,"A validade do token expirou.", Color.RED);
                            return;
                        }

                    } else {

                    	MessageUtils.updateDescriptionLabel(lbDescription,"O usuário está incorreto.", Color.RED);
                        return;

                    }

                }

            }

        });

        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "font:12");

        panel.add(lbTitle, "gapbottom 10, span, align center");
        panel.add(lbDescription, "gapbottom 20, span, align center");
        panel.add(new JLabel("Usuário:"), "gapbottom 2");
        panel.add(txtUsername, "gapbottom 10");
        panel.add(new JLabel("Senha:"), "gapbottom 2");
        panel.add(txtPassword, "gapbottom 8");

        addLoginButton(panel);

        add(panel);
    }

    private static void gravarArquivoLogin(LoginDTO loginDTO) {
        Path path = Paths.get(LOGIN_FILE);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
            if (Files.size(path) == 0) {
                writer.write("usuario|senha");
                writer.newLine();
            }

            String encryptedPass = CryptoUtils.encrypt(loginDTO.getSenha());

            loginDTO.setSenha(encryptedPass);

            writer.write(String.format("%s|%s", loginDTO.getUsuario(), loginDTO.getSenha()));
            writer.newLine();

        } catch (Exception e) {
            logger.error("Erro ao gravar arquivo de login.", e);
        }
    }

    private void carregarArquivoLicenca() {
        Path path = Paths.get(LICENCA_FILE);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("mac")) {
                    String[] campos = line.split("\\|");
                    LicencaDTO licencaDTO = new LicencaDTO(campos[0], campos[1]);
                    listaLicenca.add(licencaDTO);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar arquivo de licença.", e);
        }
    }
    
    private void carregarArquivoLogin() {
        Path path = Paths.get(LOGIN_FILE);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("usuario")) {
                    String[] campos = line.split("\\|");
                    LoginDTO loginDTO = new LoginDTO(campos[0], CryptoUtils.decrypt(campos[1]));
                    listaLogin.add(loginDTO);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar arquivo de login.", e);
        }
    }
    
    private static void gravarArquivoLicenca(LicencaDTO licencaDTO) {
        Path path = Paths.get(LICENCA_FILE);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
            if (Files.size(path) == 0) {
                writer.write("numeroserie|licenca");
                writer.newLine();
            }

            writer.write(String.format("%s|%s", licencaDTO.getNumeroSerie(), licencaDTO.getCodigoLicenca()));
            writer.newLine();

        } catch (Exception e) {
            logger.error("Erro ao gravar arquivo de login.", e);
        }
    }

    private static void showMessageWithFadeIn(JLabel label) {
        Color originalColor = label.getForeground();
        Timer timer = new Timer(20, new ActionListener() {
            private float alpha = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.05f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    ((Timer) e.getSource()).stop();
                }

                label.setForeground(new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(),
                        Math.round(alpha * 255))); // Cor vermelha com opacidade

            }
        });

        timer.start();

        Timer fadeOutTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.setDelay(20);
                timer.setInitialDelay(0);
                timer.setRepeats(true);
                timer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!timer.isRunning()) {
                            return;
                        }
                        ((Timer) e.getSource()).stop();
                    }
                });
                timer.restart();
            }
        });
        fadeOutTimer.setRepeats(false);
        fadeOutTimer.start();
    }

    private void addConfirmationFields(JPanel panel) {

    	MessageUtils.updateDescriptionLabel(lbDescription,"Cadastre sua senha.", Color.WHITE);

        txtPassword.setText("");

        lblPasswordConfirm = new JLabel("Confirmação:");
        txtPasswordConfirm.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Entre com a confirmação da senha.");

        panel.add(lblPasswordConfirm, "gapbottom 0");
        panel.add(txtPasswordConfirm, "growx, gapbottom 8");

        addLoginButton(panel);

        panel.revalidate();
        panel.repaint();

        SwingUtilities.invokeLater(() -> txtPassword.requestFocusInWindow());
    }

    private void addLoginButton(JPanel panel) {
        panel.add(cmdLogin, "gaptop 10, sizegroup btn");
        panel.revalidate();
        panel.repaint();
    }



}